package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class TechnologiesProvider extends AbstractProvider<Technologies> {

  @Override
  public Technologies provide(MavenProject project, Bom existingBom) {
    List<Technology> technologies = new ArrayList<>();

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
}
