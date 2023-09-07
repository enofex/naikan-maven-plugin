package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

@FunctionalInterface
public interface ModelConverter {

  Bom convert(MavenSession session, MavenProject project, Bom existingBom);
}
