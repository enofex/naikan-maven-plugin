package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Teams;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class TeamsProvider extends BomProvider<Teams> {

  @Override
  public Teams provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.teams() != null
        ? existingBom.teams() : Teams.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Teams.class.isAssignableFrom(clazz);
  }
}
