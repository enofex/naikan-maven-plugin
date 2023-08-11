package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Roles;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named
public class DefaultModelConverter implements ModelConverter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Bom convert(MavenProject project) {
    return Bom.builder()
        .project(project(project))
        .organization(organization(project))
        .environments(environments(project))
        .teams(teams(project))
        .developers(developers(project))
        .contacts(contacts(project))
        .technologies(technologies(project))
        .licenses(licenses(project))
        .documentations(documentations(project))
        .integrations(integrations(project))
        .tags(tags(project))
        .build();
  }

  private static Project project(MavenProject project) {
    return new Project(
        project.getName(),
        project.getUrl(),
        project.getScm() != null ? project.getScm().getUrl() : null,
        project.getPackaging(),
        project.getGroupId(),
        project.getArtifactId(),
        project.getVersion(),
        project.getDescription(),
        null
    );
  }

  private static Organization organization(MavenProject project) {
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


  private static Environments environments(MavenProject project) {
    return Environments.empty();
  }

  private static Teams teams(MavenProject project) {
    return Teams.empty();
  }

  private static Developers developers(MavenProject project) {
    List<org.apache.maven.model.Developer> developers = project.getDevelopers();

    if (developers != null) {
      return new Developers(project.getDevelopers()
          .stream()
          .map(developer -> new Developer(
              developer.getName(),
              null,
              null,
              null,
              developer.getEmail(),
              null,
              developer.getOrganization(),
              developer.getOrganizationUrl(),
              developer.getTimezone(),
              null,
              new Roles(developer.getRoles())))
          .collect(Collectors.toList()));
    }

    return Developers.empty();
  }

  private static Contacts contacts(MavenProject project) {
    return Contacts.empty();
  }

  private static Technologies technologies(MavenProject project) {
    List<Technology> technologies = new ArrayList<>();

    if (project.getProperties().getProperty("java.version") != null) {
      technologies.add(new Technology(
          "Java",
          project.getProperties().getProperty("java.version"),
          null,
          Tags.empty()));
    }

    return new Technologies(technologies);
  }

  private static Licenses licenses(MavenProject project) {
    List<org.apache.maven.model.License> licenses = project.getLicenses();

    if (licenses != null) {
      return new Licenses(project.getLicenses()
          .stream()
          .map(license -> new License(
              license.getName(),
              license.getUrl(),
              license.getComments()))
          .collect(Collectors.toList()));
    }

    return Licenses.empty();
  }

  private static Documentations documentations(MavenProject project) {
    return Documentations.empty();
  }

  private static Integrations integrations(MavenProject project) {
    List<Integration> integrations = new ArrayList<>();

    Scm scm = project.getScm();
    CiManagement ciManagement = project.getCiManagement();

    if (scm != null) {
      integrations.add(new Integration(
          "SCM",
          scm.getUrl(),
          null,
          Tags.empty()));
    }

    if (ciManagement != null) {
      integrations.add(new Integration(
          ciManagement.getSystem(),
          ciManagement.getUrl(),
          null,
          Tags.empty()));
    }

    return new Integrations(integrations);
  }

  private static Tags tags(MavenProject project) {
    return Tags.empty();
  }
}
