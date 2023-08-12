package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.project.MavenProject;

@FunctionalInterface
public interface ModelConverter {

  Bom convert(MavenProject project, Bom existingBom);
}
