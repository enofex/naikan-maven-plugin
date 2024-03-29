package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public interface Provider<T> {

  int DEFAULT_ORDER = 0;

  T provide(MavenSession session, MavenProject project, Bom existingBom);

  boolean support(Class<?> clazz);

  default int order() {
    return DEFAULT_ORDER;
  }
}
