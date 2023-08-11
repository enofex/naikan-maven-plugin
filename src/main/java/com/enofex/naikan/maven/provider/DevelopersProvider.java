package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Roles;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.model.Developer;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class DevelopersProvider extends AbstractProvider<Developers> {

  @Override
  public Developers provide(MavenProject project, Bom existingBom) {
    List<Developer> developers = project.getDevelopers();

    if (developers != null) {
      return new Developers(developers
          .stream()
          .map(developer -> new com.enofex.naikan.model.Developer(
              developer.getName(),
              null,
              null,
              null,
              developer.getEmail(),
              null,
              developer.getOrganization(),
              developer.getOrganizationUrl(),
              developer.getTimezone(),
              null,
              new Roles(developer.getRoles())))
          .collect(Collectors.toList()));
    }

    return Developers.empty();
  }
}
