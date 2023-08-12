package com.enofex.naikan.maven;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named
class DefaultModelConverter implements ModelConverter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  private Provider<Project> projectProvider;
  @Inject
  private Provider<Organization> organizationProvider;
  @Inject
  private Provider<Environments> environmentsProvider;
  @Inject
  private Provider<Teams> teamsProvider;
  @Inject
  private Provider<Developers> developersProvider;
  @Inject
  private Provider<Contacts> contactsProvider;
  @Inject
  private Provider<Technologies> technologiesProvider;
  @Inject
  private Provider<Licenses> licensesProvider;
  @Inject
  private Provider<Documentations> documentationsProvider;
  @Inject
  private Provider<Integrations> integrationsProvider;
  @Inject
  private Provider<Tags> tagsProvider;

  @Override
  public Bom convert(MavenProject project, Bom existingBom) {
    return Bom.builder()
        .project(this.projectProvider.provide(project, existingBom))
        .organization(this.organizationProvider.provide(project, existingBom))
        .environments(this.environmentsProvider.provide(project, existingBom))
        .teams(this.teamsProvider.provide(project, existingBom))
        .developers(this.developersProvider.provide(project, existingBom))
        .contacts(this.contactsProvider.provide(project, existingBom))
        .technologies(this.technologiesProvider.provide(project, existingBom))
        .licenses(this.licensesProvider.provide(project, existingBom))
        .documentations(this.documentationsProvider.provide(project, existingBom))
        .integrations(this.integrationsProvider.provide(project, existingBom))
        .tags(this.tagsProvider.provide(project, existingBom))
        .build();
  }
}
