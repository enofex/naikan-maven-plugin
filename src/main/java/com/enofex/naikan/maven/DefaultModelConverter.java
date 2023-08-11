package com.enofex.naikan.maven;

import com.enofex.naikan.maven.provider.ContactsProvider;
import com.enofex.naikan.maven.provider.DevelopersProvider;
import com.enofex.naikan.maven.provider.DocumentationsProvider;
import com.enofex.naikan.maven.provider.EnvironmentsProvider;
import com.enofex.naikan.maven.provider.IntegrationsProvider;
import com.enofex.naikan.maven.provider.LicensesProvider;
import com.enofex.naikan.maven.provider.OrganizationProvider;
import com.enofex.naikan.maven.provider.ProjectProvider;
import com.enofex.naikan.maven.provider.TagsProvider;
import com.enofex.naikan.maven.provider.TeamsProvider;
import com.enofex.naikan.maven.provider.TechnologiesProvider;
import com.enofex.naikan.model.Bom;
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
  private ProjectProvider projectProvider;
  @Inject
  private OrganizationProvider organizationProvider;
  @Inject
  private EnvironmentsProvider environmentsProvider;
  @Inject
  private TeamsProvider teamsProvider;
  @Inject
  private DevelopersProvider developersProvider;
  @Inject
  private ContactsProvider contactsProvider;
  @Inject
  private TechnologiesProvider technologiesProvider;
  @Inject
  private LicensesProvider licensesProvider;
  @Inject
  private DocumentationsProvider documentationsProvider;
  @Inject
  private IntegrationsProvider integrationsProvider;
  @Inject
  private TagsProvider tagsProvider;

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
