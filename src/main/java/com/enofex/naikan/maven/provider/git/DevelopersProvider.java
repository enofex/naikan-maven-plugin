package com.enofex.naikan.maven.provider.git;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Roles;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;

public final class DevelopersProvider extends GitProvider<Developers> {

  @Override
  public Developers provide(MavenSession session, MavenProject project, Bom existingBom) {
    org.eclipse.jgit.lib.Repository repository = gitRepository(project.getBasedir());

    if (repository != null) {
      Map<String, PersonIdent> authors = new HashMap<>();

      try (Git git = new Git(repository)) {
        Iterable<RevCommit> logCommits = git.log()
            .add(repository.resolve(repository.getFullBranch()))
            .setRevFilter(RevFilter.NO_MERGES)
            .call();

        for (RevCommit commit : logCommits) {
          authors.putIfAbsent(commit.getAuthorIdent().getEmailAddress(), commit.getAuthorIdent());
        }
      } catch (Exception e) {
        getLog().error(e);
      }

      if (!authors.isEmpty()) {
        return new Developers(authors.values()
            .stream()
            .map(author -> new Developer(
                author.getName(),
                null,
                null,
                null,
                author.getEmailAddress(),
                null,
                null,
                null,
                author.getZoneId().getId(),
                null,
                Roles.of("Contributor")))
            .toList());
      }
    }

    return Developers.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Developers.class.isAssignableFrom(clazz);
  }
}
