package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.serializer.SerializerFactory;
import java.io.File;
import java.nio.file.Path;
import javax.inject.Inject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(
    name = "aggregate",
    defaultPhase = LifecyclePhase.PACKAGE,
    threadSafe = true,
    aggregator = true,
    requiresOnline = true
)
class NaikanAggregateMojo extends AbstractMojo {

  @Parameter(property = "project", readonly = true, required = true)
  private MavenProject project;

  @Parameter(property = "outputName", defaultValue = "naikan.json")
  private String outputName;

  @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}")
  private File outputDirectory;

  @Parameter(property = "inputName", defaultValue = "naikan.json")
  private String inputName;

  @Parameter(property = "inputDirectory", defaultValue = "${project.basedir}")
  private File inputDirectory;

  @Parameter(property = "naikan.skip", defaultValue = "false")
  private boolean skip = false;

  @Inject
  private ModelConverter modelConverter;

  @Override
  public void execute() throws MojoExecutionException {
    if (isShouldSkip()) {
      getLog().info("Skipping Naikan");
      return;
    }

    getLog().info("Naikan: Creating BOM");

    Bom bom = this.modelConverter.convert(this.project);

    if (bom != null) {
      generateBom(bom);
    }
  }

  private boolean isShouldSkip() {
    return Boolean.parseBoolean(System.getProperty("naikan.skip", Boolean.toString(this.skip)));
  }

  private void generateBom(Bom bom) throws MojoExecutionException {
    try {
      Path file = Path.of(this.outputDirectory.getAbsolutePath(),this.outputName);
      getLog().info(String.format("Naikan: Writing BOM %s", file));

      SerializerFactory.newJsonSerializer().toFile(bom, file.toString());

      getLog().info(String.format("Naikan: Writing BOM %s finished", file));
    } catch (Exception e) {
      throw new MojoExecutionException("An error occurred writing BOM", e);
    }
  }
}
