package com.enofex.naikan.maven;


import com.enofex.naikan.model.AbstractContainer;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Branches;
import com.enofex.naikan.model.Contact;
import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentation;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environment;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Repository;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.model.RepositoryTags;
import com.enofex.naikan.model.Roles;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Team;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

final class ModelMerger {

  private ModelMerger() {
  }

  static <T> T merge(MavenSession session, MavenProject project, Bom existingBom, Class<T> clazz) {
    List<T> objects = ProviderFactory
        .providers(clazz)
        .stream()
        .map(provider -> provider.provide(session, project, existingBom))
        .toList();

    if (objects.size() == 1) {
      return objects.get(0);
    } else if (objects.size() > 1) {
      T object = objects.get(0);

      for (int i = 1; i < objects.size(); i++) {
        object = merge(object, objects.get(i));
      }

      return object;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private static <T> T merge(T o1, T o2) {
    if (o1 instanceof Project) {
      return (T) merge((Project) o1, (Project) o2);

    } else if (o1 instanceof Organization) {
      return (T) merge((Organization) o1, (Organization) o2);

    } else if (o1 instanceof Environments) {
      return (T) new Environments(merge((Environments) o1, (Environments) o2, Environment::name));
    } else if (o1 instanceof Environment) {
      return (T) merge((Environment) o1, (Environment) o2);

    } else if (o1 instanceof Teams) {
      return (T) new Teams(merge((Teams) o1, (Teams) o2, Team::name));
    } else if (o1 instanceof Team) {
      return (T) merge((Team) o1, (Team) o2);

    } else if (o1 instanceof Developers) {
      return (T) new Developers(merge((Developers) o1, (Developers) o2, Developer::name));
    } else if (o1 instanceof Developer) {
      return (T) merge((Developer) o1, (Developer) o2);

    } else if (o1 instanceof Contacts) {
      return (T) new Contacts(merge((Contacts) o1, (Contacts) o2, Contact::name));
    } else if (o1 instanceof Contact) {
      return (T) merge((Contact) o1, (Contact) o2);

    } else if (o1 instanceof Technologies) {
      return (T) new Technologies(merge((Technologies) o1, (Technologies) o2, Technology::name));
    } else if (o1 instanceof Technology) {
      return (T) merge((Technology) o1, (Technology) o2);

    } else if (o1 instanceof Licenses) {
      return (T) new Licenses(merge((Licenses) o1, (Licenses) o2, License::name));
    } else if (o1 instanceof License) {
      return (T) merge((License) o1, (License) o2);

    } else if (o1 instanceof Documentations) {
      return (T) new Documentations(
          merge((Documentations) o1, (Documentations) o2, Documentation::name));
    } else if (o1 instanceof Documentation) {
      return (T) merge((Documentation) o1, (Documentation) o2);


    } else if (o1 instanceof Integrations) {
      return (T) new Integrations(merge((Integrations) o1, (Integrations) o2, Integration::name));
    } else if (o1 instanceof Integration) {
      return (T) merge((Integration) o1, (Integration) o2);

    } else if (o1 instanceof Repository) {
      return (T) merge((Repository) o1, (Repository) o2);

    } else if (o1 instanceof Tags) {
      return (T) mergeTags((Tags) o1, (Tags) o2);
    }

    return o1;
  }

  private static <T> List<T> merge(AbstractContainer<T> o1, AbstractContainer<T> o2,
      Function<? super T, String> groupBy) {
    Map<String, List<T>> grouped = Stream
        .of(o1.all(), o2.all())
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(groupBy));
    List<T> objects = new ArrayList<>();

    for (Map.Entry<String, List<T>> entry : grouped.entrySet()) {
      if (entry.getValue().size() == 1) {
        objects.add(entry.getValue().get(0));
      } else if (entry.getValue().size() > 1) {
        T object = entry.getValue().get(0);

        for (int i = 1; i < entry.getValue().size(); i++) {
          object = merge(object, entry.getValue().get(i));
        }

        objects.add(object);
      }
    }

    return objects;
  }

  private static Project merge(Project p1, Project p2) {
    return new Project(
        p2.name() != null ? p2.name() : p1.name(),
        p2.inceptionYear() != null ? p2.inceptionYear() : p1.inceptionYear(),
        p2.url() != null ? p2.url() : p1.url(),
        p2.repository() != null ? p2.repository() : p1.repository(),
        p2.packaging() != null ? p2.packaging() : p1.packaging(),
        p2.groupId() != null ? p2.groupId() : p1.groupId(),
        p2.artifactId() != null ? p2.artifactId() : p1.artifactId(),
        p2.version() != null ? p2.version() : p1.version(),
        p2.description() != null ? p2.description() : p1.description(),
        p2.notes() != null ? p2.notes() : p1.notes());
  }

  private static Organization merge(Organization o1, Organization o2) {
    return new Organization(
        o2.name() != null ? o2.name() : o1.name(),
        o2.url() != null ? o2.url() : o1.url(),
        o2.department() != null ? o2.department() : o1.department(),
        o2.description() != null ? o2.description() : o1.description());
  }

  private static Environment merge(Environment e1, Environment e2) {
    if (e1.name() == null || !e1.name().equalsIgnoreCase(e2.name())) {
      return e1;
    }

    return new Environment(
        e1.name(),
        e2.location() != null ? e2.location() : e1.location(),
        e2.description() != null ? e2.description() : e1.description(),
        mergeTags(e1.tags(), e2.tags()));
  }

  private static Team merge(Team t1, Team t2) {
    if (t1.name() == null || !t1.name().equalsIgnoreCase(t2.name())) {
      return t1;
    }

    return new Team(
        t1.name(),
        t2.description() != null ? t2.description() : t1.description());
  }

  private static Developer merge(Developer d1, Developer d2) {
    if (d1.email() == null || !d1.email().equalsIgnoreCase(d2.email())) {
      return d1;
    }

    return new Developer(
        d2.name() != null ? d2.name() : d1.name(),
        d2.username() != null ? d2.username() : d1.username(),
        d2.title() != null ? d2.title() : d1.title(),
        d2.department() != null ? d2.department() : d1.department(),
        d1.email(),
        d2.phone() != null ? d2.phone() : d1.phone(),
        d2.organization() != null ? d2.organization() : d1.organization(),
        d2.organizationUrl() != null ? d2.organizationUrl() : d1.organizationUrl(),
        d2.timezone() != null ? d2.timezone() : d1.timezone(),
        d2.description() != null ? d2.description() : d1.description(),
        mergeRoles(d1.roles(), d2.roles()));
  }

  private static Contact merge(Contact c1, Contact c2) {
    if (c1.name() == null || !c1.name().equalsIgnoreCase(c2.name())) {
      return c1;
    }

    return new Contact(
        c1.name(),
        c2.title() != null ? c2.title() : c1.title(),
        c2.email() != null ? c2.email() : c1.email(),
        c2.phone() != null ? c2.phone() : c1.phone(),
        c2.description() != null ? c2.description() : c1.description(),
        mergeRoles(c1.roles(), c2.roles()));
  }

  private static Technology merge(Technology t1, Technology t2) {
    if (t1.name() == null || !t1.name().equalsIgnoreCase(t2.name())) {
      return t1;
    }

    return new Technology(
        t1.name(),
        t2.version() != null ? t2.version() : t1.version(),
        t2.description() != null ? t2.description() : t1.description(),
        mergeTags(t1.tags(), t2.tags()));
  }

  private static License merge(License l1, License l2) {
    if (l1.name() == null || !l1.name().equalsIgnoreCase(l2.name())) {
      return l1;
    }

    return new License(
        l1.name(),
        l2.url() != null ? l2.url() : l1.url(),
        l2.description() != null ? l2.description() : l1.description());
  }

  private static Documentation merge(Documentation d1, Documentation d2) {
    if (d1.name() == null || !d1.name().equalsIgnoreCase(d2.name())) {
      return d1;
    }

    return new Documentation(
        d1.name(),
        d2.location() != null ? d2.location() : d1.location(),
        d2.description() != null ? d2.description() : d1.description(),
        mergeTags(d1.tags(), d1.tags()));
  }

  private static Integration merge(Integration i1, Integration i2) {
    if (i1.name() == null || !i1.name().equalsIgnoreCase(i2.name())) {
      return i1;
    }

    return new Integration(
        i1.name(),
        i2.url() != null ? i2.url() : i1.url(),
        i2.description() != null ? i2.description() : i1.description(),
        mergeTags(i1.tags(), i2.tags()));
  }

  private static Repository merge(Repository r1, Repository r2) {
    return new Repository(
        r2.name() != null ? r2.name() : r1.name(),
        r2.url() != null ? r2.url() : r1.url(),
        r2.firstCommit() != null ? r2.firstCommit() : r1.firstCommit(),
        r2.totalCommits() > 0 ? r2.totalCommits() : r1.totalCommits(),
        r2.defaultBranch() != null ? r2.defaultBranch() : r1.defaultBranch(),
        mergeBranches(r1.branches(), r2.branches()),
        mergeRepositoryTags(r1.tags(), r2.tags()),
        r2.commits() != null ? r2.commits() : r1.commits());
  }

  private static Tags mergeTags(Tags first, Tags second) {
    return new Tags((List<String>) Stream.of(
            first != null ? first.all() : List.of(),
            second != null ? second.all() : List.of()
        )
        .flatMap(Collection::stream)
        .distinct()
        .toList());
  }

  private static Roles mergeRoles(Roles first, Roles second) {
    return new Roles((List<String>) Stream.of(
            first != null ? first.all() : List.of(),
            second != null ? second.all() : List.of()
        )
        .flatMap(Collection::stream)
        .distinct()
        .toList());
  }

  private static Branches mergeBranches(Branches first, Branches second) {
    return new Branches((List<String>) Stream.of(
            first != null ? first.all() : List.of(),
            second != null ? second.all() : List.of()
        )
        .flatMap(Collection::stream)
        .distinct()
        .toList());
  }

  private static RepositoryTags mergeRepositoryTags(RepositoryTags first,
      RepositoryTags second) {
    return new RepositoryTags((List<RepositoryTag>) Stream.of(
            first != null ? first.all() : List.of(),
            second != null ? second.all() : List.of()
        )
        .flatMap(Collection::stream)
        .distinct()
        .toList());
  }
}
