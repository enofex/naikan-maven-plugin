package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Project;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class ProjectProvider extends AbstractProvider<Project> {

  @Override
  public Project provide(MavenProject project, Bom existingBom) {
    return new Project(
        name(project, existingBom),
        url(project, existingBom),
        project.getScm() != null ? project.getScm().getUrl() : null,
        packaging(project, existingBom),
        groupId(project, existingBom),
        artifactId(project, existingBom),
        version(project, existingBom),
        description(project, existingBom),
        notes(existingBom)
    );
  }

  private static String name(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().name() != null
        ? existingBom.project().name()
        : project.getName();
  }

  private static String url(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().url() != null
        ? existingBom.project().url()
        : project.getUrl();
  }

  private static String packaging(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().packaging() != null
        ? existingBom.project().packaging()
        : project.getPackaging();
  }

  private static String groupId(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().groupId() != null
        ? existingBom.project().groupId()
        : project.getGroupId();
  }

  private static String artifactId(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().artifactId() != null
        ? existingBom.project().artifactId()
        : project.getArtifactId();
  }

  private static String version(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().version() != null
        ? existingBom.project().version()
        : project.getVersion();
  }

  private static String description(MavenProject project, Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().description() != null
        ? existingBom.project().description()
        : project.getDescription();
  }

  private static String notes(Bom existingBom) {
    return projectExists(existingBom)
        && existingBom.project().description() != null
        ? existingBom.project().description()
        : null;
  }

  private static boolean projectExists(Bom existingBom) {
    return existingBom != null
        && existingBom.project() != null;
  }
}
