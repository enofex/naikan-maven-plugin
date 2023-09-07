package com.enofex.naikan.maven.provider.git;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Tags;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class IntegrationsProvider extends GitProvider<Integrations> {

  @Override
  public Integrations provide(MavenSession session, MavenProject project, Bom existingBom) {
    List<Integration> integrations = new ArrayList<>(1);

    git(project, integrations);

    return new Integrations(integrations);
  }

  private void git(MavenProject project, List<Integration> integrations) {
    org.eclipse.jgit.lib.Repository repository = gitRepository(project.getBasedir());

    if (repository != null) {
      integrations.add(new Integration(
          "Git",
          url(repository),
          "VCS for this project.",
          Tags.of("Git", "VCS")));
    }
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Integrations.class.isAssignableFrom(clazz);
  }
}
