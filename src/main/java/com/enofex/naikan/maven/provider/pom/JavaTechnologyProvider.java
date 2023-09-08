package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class JavaTechnologyProvider extends PomProvider<Technologies> {

  @Override
  public Technologies provide(MavenSession session, MavenProject project, Bom existingBom) {
    List<Technology> technologies = new ArrayList<>(1);

    java(project, technologies);

    return new Technologies(technologies);
  }

  private void java(MavenProject project, List<Technology> technologies) {
    String version = project.getProperties().getProperty("java.version");

    if (version != null) {
      technologies.add(new Technology(
          "Java",
          version,
          "Object-oriented programming language",
          Tags.of("Backend")));
    }
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Technologies.class.isAssignableFrom(clazz);
  }
}
