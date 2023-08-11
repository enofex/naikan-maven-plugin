package com.enofex.naikan.maven;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.cyclonedx.BomGeneratorFactory;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.Property;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.eclipse.aether.RepositorySystem;


abstract class AbstractNaikanMojo extends AbstractMojo {

  @Parameter(property = "project", readonly = true, required = true)
  private MavenProject project;


  @Parameter(property = "schemaVersion", defaultValue = "1.0", required = false)
  private String schemaVersion;

  @Parameter(property = "outputName", defaultValue = "bom", required = false)
  private String outputName;

  @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}", required = false)
  private File outputDirectory;

  @org.apache.maven.plugins.annotations.Component(hint = "default")
  private RepositorySystem aetherRepositorySystem;


  @Parameter(property = "naikan.skip", defaultValue = "false", required = false)
  private boolean skip = false;


  @Parameter(property = "naikan.verbose", defaultValue = "false", required = false)
  private boolean verbose = false;

  @org.apache.maven.plugins.annotations.Component
  private MavenProjectHelper mavenProjectHelper;

  @org.apache.maven.plugins.annotations.Component
  private ModelConverter modelConverter;

  /**
   * Various messages sent to console.
   */
  protected static final String MESSAGE_RESOLVING_DEPS = "Naikan: Resolving Dependencies";
  protected static final String MESSAGE_RESOLVING_AGGREGATED_DEPS = "Naikan: Resolving Aggregated Dependencies";
  protected static final String MESSAGE_CREATING_BOM = "Naikan: Creating BOM version %s with %d component(s)";
  protected static final String MESSAGE_WRITING_BOM = "Naikan: Writing and validating BOM (%s): %s";
  protected static final String MESSAGE_ATTACHING_BOM = "           attaching as %s-%s-cyclonedx.%s";
  protected static final String MESSAGE_VALIDATION_FAILURE = "The BOM does not conform to the Naikan BOM standard as defined by the XSD";


  protected MavenProject getProject() {
    return this.project;
  }

  protected abstract String extractComponentsAndDependencies(Set<String> topLevelComponents,
      Map<String, Component> components) throws MojoExecutionException;

  @Override
  public void execute() throws MojoExecutionException {
    if (isShouldSkip()) {
      getLog().info("Skipping Naikan");
      return;
    }

    final Set<String> topLevelComponents = new LinkedHashSet<>();
    final Map<String, Component> componentMap = new LinkedHashMap<>();

    String analysis = extractComponentsAndDependencies(topLevelComponents, componentMap);

    if (analysis != null) {
      Metadata metadata = this.modelConverter.convert(this.project, schemaVersion());

      if (schemaVersion().getVersion() >= 1.3) {
        metadata.addProperty(newProperty("maven.goal", analysis));

        List<String> scopes = new ArrayList<>();

        metadata.addProperty(newProperty("maven.scopes", String.join(",", scopes)));

      }

      final Component rootComponent = metadata.getComponent();
      componentMap.remove(rootComponent.getPurl());

      generateBom(metadata, new ArrayList<>(componentMap.values()));
    }
  }

  private boolean isShouldSkip() {
    return Boolean.parseBoolean(System.getProperty("naikan.skip", Boolean.toString(this.skip)));
  }

  private Property newProperty(String name, String value) {
    Property property = new Property();
    property.setName(name);
    property.setValue(value);
    return property;
  }

  private void generateBom(Metadata metadata, List<Component> components)
      throws MojoExecutionException {
    try {
      getLog().info(String.format(MESSAGE_CREATING_BOM, this.schemaVersion, components.size()));
      final Bom bom = new Bom();
      bom.setComponents(components);

      if (schemaVersion().getVersion() >= 1.2) {
        bom.setMetadata(metadata);
        //   bom.setDependencies(dependencies);
      }

            /*if (schemaVersion().getVersion() >= 1.3) {
                if (excludeArtifactId != null && excludeTypes.length > 0) { // TODO
                    final Composition composition = new Composition();
                    composition.setAggregate(Composition.Aggregate.INCOMPLETE);
                    composition.setDependencies(Collections.singletonList(new Dependency(bom.getMetadata().getComponent().getBomRef())));
                    bom.setCompositions(Collections.singletonList(composition));
                }
            }*/

      saveBom(bom);

    } catch (GeneratorException | ParserConfigurationException | IOException e) {
      throw new MojoExecutionException(
          "An error occurred executing " + this.getClass().getName() + ": " + e.getMessage(), e);
    }
  }

  private void saveBom(Bom bom) throws ParserConfigurationException, IOException,
      GeneratorException, MojoExecutionException {

    BomJsonGenerator bomGenerator = BomGeneratorFactory.createJson(schemaVersion(), bom);

    String bomString = bomGenerator.toJsonString();
    saveBomToFile(bomString, "json", new JsonParser());
  }

  private void saveBomToFile(String bomString, String extension, Parser bomParser)
      throws IOException, MojoExecutionException {
    File bomFile = new File(this.outputDirectory, this.outputName + "." + extension);

    getLog().info(
        String.format(MESSAGE_WRITING_BOM, extension.toUpperCase(), bomFile.getAbsolutePath()));
    FileUtils.write(bomFile, bomString, StandardCharsets.UTF_8, false);

    if (!bomParser.isValid(bomFile, schemaVersion())) {
      throw new MojoExecutionException(MESSAGE_VALIDATION_FAILURE);
    }

    getLog().info(String.format(MESSAGE_ATTACHING_BOM, this.project.getArtifactId(),
        this.project.getVersion(), extension));
    this.mavenProjectHelper.attachArtifact(this.project, extension, "cyclonedx", bomFile);
  }

  protected CycloneDxSchema.Version schemaVersion() {
    if ("1.0".equals(this.schemaVersion)) {
      return CycloneDxSchema.Version.VERSION_10;
    } else if ("1.1".equals(this.schemaVersion)) {
      return CycloneDxSchema.Version.VERSION_11;
    } else if ("1.2".equals(this.schemaVersion)) {
      return CycloneDxSchema.Version.VERSION_12;
    } else if ("1.3".equals(this.schemaVersion)) {
      return CycloneDxSchema.Version.VERSION_13;
    } else {
      return CycloneDxSchema.Version.VERSION_14;
    }
  }
}
