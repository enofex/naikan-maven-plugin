package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Roles;
import java.util.List;
import java.util.stream.Stream;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class DevelopersProvider extends PomProvider<Developers> {

  @Override
  public Developers provide(MavenSession session, MavenProject project, Bom existingBom) {
    if (project.getDevelopers() != null) {
      return new Developers(project.getDevelopers()
          .stream()
          .map(developer -> new Developer(
              developer.getName(),
              developer.getId(),
              null,
              null,
              developer.getEmail(),
              null,
              developer.getOrganization(),
              developer.getOrganizationUrl(),
              developer.getTimezone(),
              null,
              new Roles(Stream.of(List.of("Developer"), developer.getRoles())
                  .flatMap(List::stream)
                  .toList())))
          .toList());
    }

    return Developers.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Developers.class.isAssignableFrom(clazz);
  }
}
