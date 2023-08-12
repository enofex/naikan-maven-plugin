package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Tags;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class IntegrationsProvider extends AbstractProvider<Integrations> {

  @Override
  public Integrations provide(MavenProject project, Bom existingBom) {
    List<Integration> integrations = new ArrayList<>();

    if (existingBom != null) {
      integrations.addAll(existingBom.integrations().all());
    }

    scm(project, integrations);
    ciManagement(project, integrations);

    return new Integrations(integrations);
  }


  private static void scm(MavenProject project, List<Integration> integrations) {
    Scm scm = project.getScm();

    if (scm != null) {
      integrations.add(new Integration(
          "SCM",
          scm.getUrl(),
          null,
          Tags.empty()));
    }
  }

  private static void ciManagement(MavenProject project, List<Integration> integrations) {
    CiManagement ciManagement = project.getCiManagement();

    if (ciManagement != null) {
      integrations.add(new Integration(
          ciManagement.getSystem(),
          ciManagement.getUrl(),
          null,
          Tags.empty()));
    }
  }
}
