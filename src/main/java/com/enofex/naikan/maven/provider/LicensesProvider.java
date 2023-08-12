package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.Licenses;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class LicensesProvider extends AbstractProvider<Licenses> {

  @Override
  public Licenses provide(MavenProject project, Bom existingBom) {
    List<License> licenses = existingBom != null
        ? new ArrayList<>(existingBom.licenses().all())
        : new ArrayList<>();

    if (project.getLicenses() != null) {
      return new Licenses(project.getLicenses()
          .stream()
          .map(license -> new com.enofex.naikan.model.License(
              license.getName(),
              license.getUrl(),
              license.getComments()))
          .collect(Collectors.toList()));
    }

    return new Licenses(licenses);
  }
}
