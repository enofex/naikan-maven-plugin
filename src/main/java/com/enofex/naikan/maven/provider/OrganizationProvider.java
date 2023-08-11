package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Organization;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class OrganizationProvider extends AbstractProvider<Organization> {

  @Override
  public Organization provide(MavenProject project, Bom existingBom) {
    org.apache.maven.model.Organization organization = project.getOrganization();

    if (organization != null) {
      return new Organization(
          organization.getName(),
          organization.getUrl(),
          null,
          null
      );
    }

    return null;
  }
}
