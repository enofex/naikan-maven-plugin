package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Documentations;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class DocumentationsProvider extends AbstractProvider<Documentations> {

  @Override
  public Documentations provide(MavenProject project, Bom existingBom) {
    return existingBom != null ? existingBom.documentations() : Documentations.empty();
  }
}
