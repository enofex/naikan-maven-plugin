package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public final class SpringBootStarterParentTechnologyProvider extends PomProvider<Technologies> {

  private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";
  private static final String SPRING_BOOT_ARTIFACT_ID = "spring-boot-starter-parent";

  @Override
  public Technologies provide(MavenSession session, MavenProject project, Bom existingBom) {
    List<Technology> technologies = new ArrayList<>(1);

    while (project != null) {
      project = project.getParent();

      if (project != null && isSpringBootParent(project)) {
        technologies.add(new Technology(
            "Spring Boot",
            project.getParent().getVersion(),
            "Spring Boot Starter Parent",
            Tags.of("Backend")));
        break;
      }
    }

    return new Technologies(technologies);
  }

  private boolean isSpringBootParent(MavenProject project) {
    return SPRING_BOOT_GROUP_ID.equalsIgnoreCase(project.getGroupId()) &&
        SPRING_BOOT_ARTIFACT_ID.equalsIgnoreCase(project.getArtifactId());
  }

  @Override
  public boolean support(Class<?> clazz) {
    return Technologies.class.isAssignableFrom(clazz);
  }
}
