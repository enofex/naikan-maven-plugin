package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Developers;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class DevelopersProvider extends BomProvider<Developers> {

  @Override
  public Developers provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.developers() != null
        ? existingBom.developers() : Developers.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Developers.class.isAssignableFrom(clazz);
  }
}
