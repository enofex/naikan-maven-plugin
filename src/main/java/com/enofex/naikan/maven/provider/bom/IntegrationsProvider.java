package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Integrations;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class IntegrationsProvider extends BomProvider<Integrations> {

  @Override
  public Integrations provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.integrations() != null
        ? existingBom.integrations() : Integrations.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Integrations.class.isAssignableFrom(clazz);
  }
}
