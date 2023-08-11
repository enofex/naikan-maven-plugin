package com.enofex.naikan.maven;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.cyclonedx.model.Component;

@Mojo(
    name = "makeAggregateBom",
    defaultPhase = LifecyclePhase.PACKAGE,
    threadSafe = true,
    aggregator = true,
    requiresOnline = true
)
class NaikanAggregateMojo extends AbstractNaikanMojo {

  @Parameter(property = "reactorProjects", readonly = true, required = true)
  private List<MavenProject> reactorProjects;

  @Override
  protected String extractComponentsAndDependencies(Set<String> topLevelComponents,
      Map<String, Component> components) {

    if (!getProject().isExecutionRoot()) {
      getLog().info("Skipping Naikan on non-execution root");
      return null;
    }

    getLog().info((this.reactorProjects.size() <= 1)
        ? MESSAGE_RESOLVING_DEPS
        : MESSAGE_RESOLVING_AGGREGATED_DEPS);

    for (MavenProject mavenProject : this.reactorProjects) {
      //Component projectBomComponent = convert(mavenProject.getArtifact());
      //components.put(projectBomComponent.getPurl(), projectBomComponent);
      //topLevelComponents.add(projectBomComponent.getPurl());
    }

    return "makeAggregateBom";
  }
}
