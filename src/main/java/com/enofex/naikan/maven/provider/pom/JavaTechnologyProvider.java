package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.project.MavenProject;

public final class JavaTechnologyProvider extends PomProvider<Technologies> {

  @Override
  public Technologies provide(MavenProject project, Bom existingBom) {
    List<Technology> technologies = new ArrayList<>(1);

    property(project, technologies, "Java", "java.version");

    return new Technologies(technologies);
  }

  private static void property(MavenProject project, List<Technology> technologies, String name,
      String key) {
    String property = project.getProperties().getProperty(key);

    if (property != null) {
      technologies.add(new Technology(name, property, null, Tags.empty()));
    }
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Technologies.class.isAssignableFrom(clazz);
  }
}
