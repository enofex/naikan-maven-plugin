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
    Organization organization = null;

    if (project.getOrganization() != null) {
      organization = new Organization(
          name(project, existingBom),
          url(project, existingBom),
          department(existingBom),
          description(existingBom)
      );
    }

    return organization;
  }

  private static String name(MavenProject project, Bom existingBom) {
    return organizationExists(existingBom)
        && existingBom.organization().name() != null
        ? existingBom.organization().name()
        : project.getOrganization().getName();
  }

  private static String url(MavenProject project, Bom existingBom) {
    return organizationExists(existingBom)
        && existingBom.organization().url() != null
        ? existingBom.organization().url()
        : project.getOrganization().getUrl();
  }

  private static String department(Bom existingBom) {
    return organizationExists(existingBom)
        && existingBom.organization().department() != null
        ? existingBom.organization().department()
        : null;
  }

  private static String description(Bom existingBom) {
    return organizationExists(existingBom)
        && existingBom.organization().description() != null
        ? existingBom.organization().description()
        : null;
  }

  private static boolean organizationExists(Bom existingBom) {
    return existingBom != null
        && existingBom.organization() != null;
  }
  
}
