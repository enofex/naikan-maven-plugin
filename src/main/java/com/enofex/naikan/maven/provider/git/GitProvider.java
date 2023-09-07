package com.enofex.naikan.maven.provider.git;

import com.enofex.naikan.maven.AbstractProvider;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

public abstract class GitProvider<T> extends AbstractProvider<T> {

  public static final int GIT_ORDER = -536870912;

  public Repository gitRepository(File baseDirectory) {
    try {
      return new RepositoryBuilder().findGitDir(baseDirectory).build();
    } catch (IOException e) {
      getLog().info("Git repository could not be found!");
      return null;
    }
  }

  public String url(org.eclipse.jgit.lib.Repository repository) {
    return repository
        .getConfig()
        .getString(ConfigConstants.CONFIG_REMOTE_SECTION, "origin", ConfigConstants.CONFIG_KEY_URL);
  }

  public LocalDateTime commitDate(int timestamp) {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
  }

  @Override
  public int order() {
    return GIT_ORDER;
  }
}
