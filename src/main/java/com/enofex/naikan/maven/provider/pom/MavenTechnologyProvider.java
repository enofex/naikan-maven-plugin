package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public class MavenTechnologyProvider extends PomProvider<Technologies> {

  @Override
  public Technologies provide(MavenSession session, MavenProject project, Bom existingBom) {
    List<Technology> technologies = new ArrayList<>(1);

    maven(session, project, technologies);

    return new Technologies(technologies);
  }

  private void maven(MavenSession session, MavenProject project,
      List<Technology> technologies) {
    String mavenVersion = project.getProperties().getProperty("maven.version");

    if (mavenVersion == null) {
      mavenVersion = session.getSystemProperties().getProperty("maven.version");
    }

    if (mavenVersion != null) {
      technologies.add(new Technology(
          "Maven",
          mavenVersion,
          "The build automation tool for this project.",
          Tags.of("Build automation tool")));
    }
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Technologies.class.isAssignableFrom(clazz);
  }
}
