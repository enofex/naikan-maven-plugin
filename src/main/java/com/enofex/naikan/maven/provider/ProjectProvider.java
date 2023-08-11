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
}
