package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.model.serializer.SerializerFactory;
import java.io.File;
import java.nio.file.Path;
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

  @Parameter(property = "outputFileName", defaultValue = "naikan.json")
  private String outputFileName;

  @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}")
  private File outputDirectory;

  @Parameter(property = "inputFileName", defaultValue = "naikan.json")
  private String inputFileName;

  @Parameter(property = "inputDirectory", defaultValue = "${project.basedir}")
  private File inputDirectory;

  @Parameter(property = "skip", defaultValue = "false")
  private boolean skip;

  private final ModelConverter modelConverter;

  NaikanAggregateMojo() {
    this.modelConverter = new DefaultModelConverter();
  }

  @Override
  public void execute() throws MojoExecutionException {
    if (isShouldSkip()) {
      getLog().info("Naikan: Skipping");
      return;
    }

    Path path = path(this.inputDirectory, this.inputFileName);
    getLog().info(String.format("Naikan: Searching for existing BOM %s", path));
    Bom existingBom = null;

    if (path.toFile().exists()) {
      getLog().info(String.format("Naikan: Found BOM %s", path));
      existingBom = DeserializerFactory.newJsonDeserializer().of(path.toFile());
    }

    getLog().info("Naikan: Creating BOM");

    Bom bom = this.modelConverter.convert(this.project, existingBom);

    if (bom != null) {
      generateBom(bom);
    }
  }

  private boolean isShouldSkip() {
    return Boolean.parseBoolean(System.getProperty("naikan.skip", Boolean.toString(this.skip)));
  }

  private void generateBom(Bom bom) throws MojoExecutionException {
    try {
      if (!this.outputDirectory.exists() && !this.outputDirectory.mkdir()) {
        getLog().info(String.format("Naikan: Creating directory %s failed", this.outputDirectory));
      }

      Path path = path(this.outputDirectory, this.outputFileName);
      getLog().info(String.format("Naikan: Writing BOM %s", path));

      SerializerFactory.newJsonSerializer().toFile(bom, path.toString());

      getLog().info(String.format("Naikan: Writing BOM %s finished", path));
    } catch (Exception e) {
      throw new MojoExecutionException("Naikan: An error occurred writing BOM", e);
    }
  }

  private Path path(File directory, String fileName) {
    return Path.of(directory.getAbsolutePath(), fileName);
  }
}
