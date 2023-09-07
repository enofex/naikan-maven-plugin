package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class TagsProvider extends BomProvider<Tags> {

  @Override
  public Tags provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.tags() != null
        ? existingBom.tags() : Tags.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Tags.class.isAssignableFrom(clazz);
  }
}
