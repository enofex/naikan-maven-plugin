package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contacts;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class ContactsProvider extends BomProvider<Contacts> {

  @Override
  public Contacts provide(MavenSession session, MavenProject project, Bom existingBom) {
    return existingBom != null && existingBom.contacts() != null
        ? existingBom.contacts() : Contacts.empty();
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Contacts.class.isAssignableFrom(clazz);
  }
}
