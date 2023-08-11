package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Environments;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class EnvironmentsProvider extends AbstractProvider<Environments> {

  @Override
  public Environments provide(MavenProject project, Bom existingBom) {
    return existingBom != null ? existingBom.environments() : Environments.empty();
  }
}
