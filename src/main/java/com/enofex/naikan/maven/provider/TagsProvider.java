package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class TagsProvider extends AbstractProvider<Tags> {

  @Override
  public Tags provide(MavenProject project, Bom existingBom) {
    return existingBom != null ? existingBom.tags() : Tags.empty();
  }
}
