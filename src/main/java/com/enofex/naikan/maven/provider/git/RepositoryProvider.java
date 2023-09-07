package com.enofex.naikan.maven.provider.git;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Branches;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.CommitAuthor;
import com.enofex.naikan.model.CommitChanges;
import com.enofex.naikan.model.CommitFilesChanges;
import com.enofex.naikan.model.CommitLinesChanges;
import com.enofex.naikan.model.Commits;
import com.enofex.naikan.model.Repository;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.model.RepositoryTags;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public final class RepositoryProvider extends GitProvider<Repository> {

  @Override
  public Repository provide(MavenSession session, MavenProject project, Bom existingBom) {
    org.eclipse.jgit.lib.Repository repository = gitRepository(project.getBasedir());

    if (repository != null) {
      return new Repository(
          name(repository),
          url(repository),
          firstCommit(repository),
          totalCommits(repository),
          defaultBranch(repository),
          branches(repository),
          tags(repository),
          commits(repository));
    }

    return null;
  }

  private String name(org.eclipse.jgit.lib.Repository repository) {
    String url = url(repository);

    if (url != null) {
      String[] parts = url.split("/|:");
      String repositoryWithExtension = parts[parts.length - 1];
      int dotIndex = repositoryWithExtension.lastIndexOf('.');

      return dotIndex != -1
          ? repositoryWithExtension.substring(0, dotIndex)
          : repositoryWithExtension;
    }

    return null;
  }

  private int totalCommits(org.eclipse.jgit.lib.Repository repository) {
    try (Git git = new Git(repository)) {
      AtomicInteger count = new AtomicInteger();

      git.log().add(repository.resolve(repository.getFullBranch()))
          .setRevFilter(RevFilter.NO_MERGES).call()
          .forEach(revCommit -> count.getAndIncrement());

      return count.get();
    } catch (Exception e) {
      getLog().error(e);
    }

    return 0;
  }

  private Commit firstCommit(org.eclipse.jgit.lib.Repository repository) {
    try (RevWalk revWalk = new RevWalk(repository)) {
      revWalk.markStart(revWalk.parseCommit(repository.resolve(Constants.HEAD)));
      revWalk.sort(RevSort.COMMIT_TIME_DESC);
      revWalk.sort(RevSort.REVERSE);

      RevCommit commit = revWalk.next();

      if (commit != null) {
        return commit(repository, commit);
      }
    } catch (Exception e) {
      getLog().error(e);
    }

    return null;
  }

  private String defaultBranch(org.eclipse.jgit.lib.Repository repository) {
    try {
      return repository.getBranch();
    } catch (Exception e) {
      getLog().error(e);
    }

    return null;
  }

  private RepositoryTags tags(org.eclipse.jgit.lib.Repository repository) {
    try (Git git = new Git(repository)) {
      Collection<Ref> tags = git.tagList().call();
      List<RepositoryTag> repositoryTags = new ArrayList<>(tags.size());

      try (RevWalk revWalk = new RevWalk(repository)) {
        for (Ref tag : tags) {
          int timestamp = 0;
          RevObject revObject = revWalk.parseAny(tag.getObjectId());

          if (revObject instanceof RevCommit) {
            timestamp = ((RevCommit) revObject).getCommitTime();
          } else if (revObject instanceof RevTag) {
            RevObject targetObject = revWalk.parseAny(((RevTag) revObject).getObject());

            if (targetObject instanceof RevCommit) {
              timestamp = ((RevCommit) targetObject).getCommitTime();
            }
          }

          repositoryTags.add(new RepositoryTag(
              tag.getName(),
              commitDate(timestamp))
          );
        }
      }

      return new RepositoryTags(repositoryTags);
    } catch (Exception e) {
      getLog().error(e);
    }

    return RepositoryTags.empty();
  }

  private Branches branches(org.eclipse.jgit.lib.Repository repository) {
    try (Git git = new Git(repository)) {
      return new Branches(git.branchList()
          .setListMode(ListMode.REMOTE)
          .call()
          .stream()
          .map(Ref::getName)
          .toList());
    } catch (Exception e) {
      getLog().error(e);
    }

    return Branches.empty();
  }

  private Commits commits(org.eclipse.jgit.lib.Repository repository) {
    List<Commit> commits = new ArrayList<>(1000);

    try (Git git = new Git(repository)) {
      Iterable<RevCommit> logCommits = git.log()
          .add(repository.resolve(repository.getFullBranch()))
          .setRevFilter(RevFilter.NO_MERGES)
          .call();

      for (RevCommit commit : logCommits) {
        String email = commit.getAuthorIdent().getEmailAddress();

        if (email != null && !email.isBlank()) {
          commits.add(commit(repository, commit));
        }
      }

      return new Commits(commits);
    } catch (Exception e) {
      getLog().error(e);
    }

    return Commits.empty();
  }

  private Commit commit(org.eclipse.jgit.lib.Repository repository, RevCommit commit)
      throws IOException {
    RevCommit parent = commit.getParentCount() > 0 ? commit.getParent(0) : null;
    int added = 0;
    int deleted = 0;
    Set<String> addedFiles = new HashSet<>();
    Set<String> deletedFiles = new HashSet<>();
    Set<String> changedFiles = new HashSet<>();

    try (DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
      diffFormatter.setRepository(repository);
      diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
      diffFormatter.setContext(0);
      List<DiffEntry> diffEntries = diffFormatter.scan(parent, commit);

      for (DiffEntry entry : diffEntries) {
        String path;
        if (entry.getChangeType() == DiffEntry.ChangeType.DELETE) {
          path = entry.getOldPath();
        } else {
          path = entry.getNewPath();
        }

        if (entry.getChangeType() == DiffEntry.ChangeType.MODIFY ||
            entry.getChangeType() == DiffEntry.ChangeType.RENAME) {
          changedFiles.add(path);
        } else if (entry.getChangeType() == DiffEntry.ChangeType.ADD ||
            entry.getChangeType() == DiffEntry.ChangeType.COPY) {
          addedFiles.add(path);
        } else if (entry.getChangeType() == DiffEntry.ChangeType.DELETE) {
          deletedFiles.add(path);
        }

        FileHeader fileHeader = diffFormatter.toFileHeader(entry);
        List<? extends HunkHeader> hunks = fileHeader.getHunks();

        for (HunkHeader hunk : hunks) {
          EditList edits = hunk.toEditList();
          for (Edit edit : edits) {
            switch (edit.getType()) {
              case INSERT:
                added += edit.getLengthB();
                break;

              case DELETE:
                deleted += edit.getLengthA();
                break;

              case REPLACE:
                deleted += edit.getLengthA();
                added += edit.getLengthB();
                break;

              case EMPTY:
                break;
            }
          }
        }
      }
    }

    return new Commit(
        commit.getId().getName(),
        commitDate(commit.getCommitTime()),
        commit.getShortMessage(),
        new CommitAuthor(
            commit.getAuthorIdent().getName(),
            commit.getAuthorIdent().getEmailAddress()),
        new CommitChanges(
            new CommitLinesChanges(
                added,
                deleted),
            new CommitFilesChanges(
                addedFiles.size(),
                deletedFiles.size(),
                changedFiles.size()))
    );
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Repository.class.isAssignableFrom(clazz);
  }
}
