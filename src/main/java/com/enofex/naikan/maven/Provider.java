package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.project.MavenProject;

public interface Provider<T> {

  T provide(MavenProject project, Bom existingBom);

}
