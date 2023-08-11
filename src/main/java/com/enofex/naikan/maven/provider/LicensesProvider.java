package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Licenses;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class LicensesProvider extends AbstractProvider<Licenses> {

  @Override
  public Licenses provide(MavenProject project, Bom existingBom) {
    List<License> licenses = project.getLicenses();

    if (licenses != null) {
      return new Licenses(licenses
          .stream()
          .map(license -> new com.enofex.naikan.model.License(
              license.getName(),
              license.getUrl(),
              license.getComments()))
          .collect(Collectors.toList()));
    }

    return Licenses.empty();
  }
}
