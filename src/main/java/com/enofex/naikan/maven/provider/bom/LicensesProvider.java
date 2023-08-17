package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Licenses;
import org.apache.maven.project.MavenProject;

public final class LicensesProvider extends BomProvider<Licenses> {

  @Override
  public Licenses provide(MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.licenses() != null
        ? existingBom.licenses() : Licenses.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Licenses.class.isAssignableFrom(clazz);
  }
}
