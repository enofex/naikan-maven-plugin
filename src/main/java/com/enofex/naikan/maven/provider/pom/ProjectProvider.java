package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Project;
import org.apache.maven.project.MavenProject;

public final class ProjectProvider extends PomProvider<Project> {

  @Override
  public Project provide(MavenProject project, Bom existingBom) {
    if (project != null) {
      return new Project(
          project.getName(),
          project.getInceptionYear(),
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

    return null;
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Project.class.isAssignableFrom(clazz);
  }
}
