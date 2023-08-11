package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import org.apache.maven.project.MavenProject;

public interface ModelConverter {

  Bom convert(MavenProject project);
}
