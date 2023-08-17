package com.enofex.naikan.maven;

import static com.enofex.naikan.maven.Merger.merge;

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
import org.apache.maven.project.MavenProject;

class DefaultModelConverter implements ModelConverter {

  @Override
  public Bom convert(MavenProject project, Bom existingBom) {
    return Bom.builder()
        .project(merge(project, existingBom, Project.class))
        .organization(merge(project, existingBom, Organization.class))
        .environments(merge(project, existingBom, Environments.class))
        .teams(merge(project, existingBom, Teams.class))
        .developers(merge(project, existingBom, Developers.class))
        .contacts(merge(project, existingBom, Contacts.class))
        .technologies(merge(project, existingBom, Technologies.class))
        .licenses(merge(project, existingBom, Licenses.class))
        .documentations(merge(project, existingBom, Documentations.class))
        .integrations(merge(project, existingBom, Integrations.class))
        .tags(merge(project, existingBom, Tags.class))
        .build();
  }
}
