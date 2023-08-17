package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.Licenses;
import org.apache.maven.project.MavenProject;

public final class LicensesProvider extends PomProvider<Licenses> {

  @Override
  public Licenses provide(MavenProject project, Bom existingBom) {
    if (project.getLicenses() != null) {
      return new Licenses(project.getLicenses()
          .stream()
          .map(license -> new License(
              license.getName(),
              license.getUrl(),
              license.getComments()))
          .toList());
    }

    return Licenses.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Licenses.class.isAssignableFrom(clazz);
  }
}
