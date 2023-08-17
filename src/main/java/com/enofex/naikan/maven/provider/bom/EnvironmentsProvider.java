package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Environments;
import org.apache.maven.project.MavenProject;

public final class EnvironmentsProvider extends BomProvider<Environments> {

  @Override
  public Environments provide(MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.environments() != null
        ? existingBom.environments() : Environments.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Environments.class.isAssignableFrom(clazz);
  }
}
