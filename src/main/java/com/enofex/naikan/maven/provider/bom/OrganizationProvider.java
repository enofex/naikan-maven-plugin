package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Organization;
import org.apache.maven.project.MavenProject;

public final class OrganizationProvider extends BomProvider<Organization> {

  @Override
  public Organization provide(MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.organization() != null
        ? existingBom.organization() : null;
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Organization.class.isAssignableFrom(clazz);
  }
}
