package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Tags;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;

public final class IntegrationsProvider extends PomProvider<Integrations> {

  @Override
  public Integrations provide(MavenSession session, MavenProject project, Bom existingBom) {
    List<Integration> integrations = new ArrayList<>(5);

    scm(project, integrations);
    ciManagement(project, integrations);
    site(project, integrations);
    repository(project, integrations);
    snapshotRepository(project, integrations);

    return new Integrations(integrations);
  }

  private void scm(MavenProject project, List<Integration> integrations) {
    Scm scm = project.getScm();

    if (scm != null) {
      integrations.add(new Integration(
          "SCM",
          scm.getUrl(),
          "The SCM (Source Control Management) of the project.",
          Tags.of("SCM")));
    }
  }

  private void ciManagement(MavenProject project, List<Integration> integrations) {
    CiManagement ciManagement = project.getCiManagement();

    if (ciManagement != null) {
      integrations.add(new Integration(
          ciManagement.getSystem(),
          ciManagement.getUrl(),
          "The CI system of the project.",
          Tags.of("CI")));
    }
  }

  private void site(MavenProject project, List<Integration> integrations) {
    DistributionManagement distributionManagement = project.getDistributionManagement();

    if (distributionManagement != null && distributionManagement.getSite() != null) {
      integrations.add(new Integration(
          distributionManagement.getSite().getName(),
          distributionManagement.getSite().getUrl(),
          null,
          Tags.of("Site")));
    }
  }

  private void repository(MavenProject project, List<Integration> integrations) {
    DistributionManagement distributionManagement = project.getDistributionManagement();

    if (distributionManagement != null && distributionManagement.getRepository() != null) {
      integrations.add(new Integration(
          distributionManagement.getRepository().getName(),
          distributionManagement.getRepository().getUrl(),
          "Deployment remote repository for this project.",
          Tags.of("Distribution Management")));
    }
  }

  private void snapshotRepository(MavenProject project, List<Integration> integrations) {
    DistributionManagement distributionManagement = project.getDistributionManagement();

    if (distributionManagement != null && distributionManagement.getSnapshotRepository() != null) {
      integrations.add(new Integration(
          distributionManagement.getSnapshotRepository().getName(),
          distributionManagement.getSnapshotRepository().getUrl(),
          "Deployment remote snapshot repository for this project.",
          Tags.of("Snapshot Distribution Management")));
    }
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Integrations.class.isAssignableFrom(clazz);
  }
}
