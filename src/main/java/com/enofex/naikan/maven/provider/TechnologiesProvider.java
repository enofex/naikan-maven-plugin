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

    java(project, technologies);

    return new Technologies(technologies);
  }

  private static void java(MavenProject project, List<Technology> technologies) {
    if (project.getProperties().getProperty("java.version") != null) {
      technologies.add(new Technology(
          "Java",
          project.getProperties().getProperty("java.version"),
          null,
          Tags.empty()));
    }
  }
}
