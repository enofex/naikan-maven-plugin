package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Documentations;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class DocumentationsProvider extends BomProvider<Documentations> {

  @Override
  public Documentations provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.documentations() != null
        ? existingBom.documentations() : Documentations.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Documentations.class.isAssignableFrom(clazz);
  }
}
