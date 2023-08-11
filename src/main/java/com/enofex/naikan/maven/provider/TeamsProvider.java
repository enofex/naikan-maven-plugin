package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Teams;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class TeamsProvider extends AbstractProvider<Teams> {

  @Override
  public Teams provide(MavenProject project, Bom existingBom) {
    return existingBom != null ? existingBom.teams() : Teams.empty();
  }
}
