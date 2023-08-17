package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.project.MavenProject;

public interface Provider<T> {

  int POM_ORDER = -1073741824;
  int DEFAULT_ORDER = 0;
  int BOM_ORDER = 1073741824;

  T provide(MavenProject project, Bom existingBom);

  boolean support(Class<?> clazz);

  default int order() {
    return DEFAULT_ORDER;
  }
}
