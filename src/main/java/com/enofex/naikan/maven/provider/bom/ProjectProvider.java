package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import org.apache.maven.project.MavenProject;

public final class ProjectProvider extends BomProvider<Project> {

  @Override
  public Project provide(MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.project() != null
        ? existingBom.project() : null;
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Organization.class.isAssignableFrom(clazz);
  }
}
