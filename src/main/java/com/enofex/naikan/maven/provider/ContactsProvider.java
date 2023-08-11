package com.enofex.naikan.maven.provider;

import com.enofex.naikan.maven.AbstractProvider;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contacts;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.project.MavenProject;

@Singleton
@Named
public class ContactsProvider extends AbstractProvider<Contacts> {

  @Override
  public Contacts provide(MavenProject project, Bom existingBom) {
    return existingBom != null ? existingBom.contacts() : Contacts.empty();
  }
}
