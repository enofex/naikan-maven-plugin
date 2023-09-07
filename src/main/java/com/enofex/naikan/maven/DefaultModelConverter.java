package com.enofex.naikan.maven;

import static com.enofex.naikan.maven.ModelMerger.merge;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Repository;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

class DefaultModelConverter implements ModelConverter {

  @Override
  public Bom convert(MavenSession session, MavenProject project, Bom existingBom) {
    return Bom.builder()
        .project(merge(session, project, existingBom, Project.class))
        .organization(merge(session, project, existingBom, Organization.class))
        .environments(merge(session, project, existingBom, Environments.class))
        .teams(merge(session, project, existingBom, Teams.class))
        .developers(merge(session, project, existingBom, Developers.class))
        .contacts(merge(session, project, existingBom, Contacts.class))
        .technologies(merge(session, project, existingBom, Technologies.class))
        .licenses(merge(session, project, existingBom, Licenses.class))
        .documentations(merge(session, project, existingBom, Documentations.class))
        .integrations(merge(session, project, existingBom, Integrations.class))
        .tags(merge(session, project, existingBom, Tags.class))
        .repository(merge(session, project, existingBom, Repository.class))
        .build();
  }
}
