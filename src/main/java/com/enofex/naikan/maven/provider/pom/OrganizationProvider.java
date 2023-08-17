package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Organization;
import org.apache.maven.project.MavenProject;

public final class OrganizationProvider extends PomProvider<Organization> {

  @Override
  public Organization provide(MavenProject project, Bom existingBom) {
    if (project != null && project.getOrganization() != null) {
      return new Organization(
          project.getOrganization().getName(),
          project.getOrganization().getUrl(),
          null,
          null
      );
    }

    return null;
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Organization.class.isAssignableFrom(clazz);
  }
}
