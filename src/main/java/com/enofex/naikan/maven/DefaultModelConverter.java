package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named
class DefaultModelConverter implements ModelConverter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  private MavenSession session;
  @Inject
  private RepositorySystem repositorySystem;
  @Inject
  private ProjectBuilder mavenProjectBuilder;

  @Override
  public Bom convert(MavenProject project) {
    return Bom.builder().build();
  }
}
