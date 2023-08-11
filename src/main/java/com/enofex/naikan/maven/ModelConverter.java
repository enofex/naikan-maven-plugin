package com.enofex.naikan.maven;

import org.apache.maven.project.MavenProject;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Metadata;

public interface ModelConverter {

  Metadata convert(MavenProject project, CycloneDxSchema.Version schemaVersion);
}
