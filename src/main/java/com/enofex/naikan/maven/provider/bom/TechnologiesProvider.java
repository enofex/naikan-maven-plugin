package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Technologies;
import org.apache.maven.project.MavenProject;

public final class TechnologiesProvider extends BomProvider<Technologies> {

  @Override
  public Technologies provide(MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.technologies() != null
        ? existingBom.technologies() : Technologies.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Technologies.class.isAssignableFrom(clazz);
  }
}
