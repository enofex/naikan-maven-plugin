package com.enofex.naikan.maven;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.repository.RepositorySystem;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.Tool;
import org.cyclonedx.util.BomUtils;
import org.cyclonedx.util.LicenseResolver;
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

  /**
   * Extracts data from a project and adds the data to the component.
   *
   * @param project   the project to extract data from
   * @param component the component to add data to
   */
  private void extractComponentMetadata(MavenProject project, Component component,
      CycloneDxSchema.Version schemaVersion) {
    if (component.getPublisher() == null) {
      // If we don't already have publisher information, retrieve it.
      if (project.getOrganization() != null) {
        component.setPublisher(project.getOrganization().getName());
      }
    }
    if (component.getDescription() == null) {
      // If we don't already have description information, retrieve it.
      component.setDescription(project.getDescription());
    }
    if (component.getLicenseChoice() == null || component.getLicenseChoice().getLicenses() == null
        || component.getLicenseChoice().getLicenses().isEmpty()) {
      // If we don't already have license information, retrieve it.
      if (project.getLicenses() != null) {
        component.setLicenseChoice(resolveMavenLicenses(project.getLicenses(), schemaVersion));
      }
    }
    if (CycloneDxSchema.Version.VERSION_10 != schemaVersion) {
      addExternalReference(ExternalReference.Type.WEBSITE, project.getUrl(), component);
      if (project.getCiManagement() != null) {
        addExternalReference(ExternalReference.Type.BUILD_SYSTEM,
            project.getCiManagement().getUrl(), component);
      }
      if (project.getDistributionManagement() != null) {
        addExternalReference(ExternalReference.Type.DISTRIBUTION,
            project.getDistributionManagement().getDownloadUrl(), component);
        if (project.getDistributionManagement().getRepository() != null) {
          addExternalReference(ExternalReference.Type.DISTRIBUTION,
              project.getDistributionManagement().getRepository().getUrl(), component);
        }
      }
      if (project.getIssueManagement() != null) {
        addExternalReference(ExternalReference.Type.ISSUE_TRACKER,
            project.getIssueManagement().getUrl(), component);
      }
      if (project.getMailingLists() != null && project.getMailingLists().size() > 0) {
        for (MailingList list : project.getMailingLists()) {
          String url = list.getArchive();
          if (url == null) {
            url = list.getSubscribe();
          }
          addExternalReference(ExternalReference.Type.MAILING_LIST, url, component);
        }
      }
      if (project.getScm() != null) {
        addExternalReference(ExternalReference.Type.VCS, project.getScm().getUrl(), component);
      }
    }
  }

  /**
   * This method generates an 'effective pom' for an artifact.
   *
   * @param artifact the artifact to generate an effective pom of
   * @throws ProjectBuildingException if an error is encountered
   */
  private MavenProject getEffectiveMavenProject(final Artifact artifact)
      throws ProjectBuildingException {
    final Artifact pomArtifact = this.repositorySystem.createProjectArtifact(artifact.getGroupId(),
        artifact.getArtifactId(), artifact.getVersion());
    final ProjectBuildingResult build = this.mavenProjectBuilder.build(pomArtifact,
        this.session.getProjectBuildingRequest()
            .setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL)
            .setProcessPlugins(false)
    );
    return build.getProject();
  }

  private void addExternalReference(final ExternalReference.Type referenceType, final String url,
      final Component component) {
    if (url == null || doesComponentHaveExternalReference(component, referenceType)) {
      return;
    }
    try {
      final URI uri = new URI(url.trim());
      final ExternalReference ref = new ExternalReference();
      ref.setType(referenceType);
      ref.setUrl(uri.toString());
      component.addExternalReference(ref);
    } catch (URISyntaxException e) {
      // throw it away
    }
  }

  private boolean doesComponentHaveExternalReference(final Component component,
      final ExternalReference.Type referenceType) {
    if (component.getExternalReferences() != null && !component.getExternalReferences().isEmpty()) {
      for (final ExternalReference ref : component.getExternalReferences()) {
        if (referenceType == ref.getType()) {
          return true;
        }
      }
    }
    return false;
  }

  private LicenseChoice resolveMavenLicenses(
      final List<org.apache.maven.model.License> projectLicenses,
      final CycloneDxSchema.Version schemaVersion) {
    final LicenseChoice licenseChoice = new LicenseChoice();
    for (org.apache.maven.model.License artifactLicense : projectLicenses) {
      boolean resolved = false;
      if (artifactLicense.getName() != null) {
        final LicenseChoice resolvedByName =
            LicenseResolver.resolve(artifactLicense.getName());
        resolved = resolveLicenseInfo(licenseChoice, resolvedByName, schemaVersion);
      }
      if (artifactLicense.getUrl() != null && !resolved) {
        final LicenseChoice resolvedByUrl =
            LicenseResolver.resolve(artifactLicense.getUrl());
        resolved = resolveLicenseInfo(licenseChoice, resolvedByUrl, schemaVersion);
      }
      if (artifactLicense.getName() != null && !resolved) {
        final License license = new License();
        license.setName(artifactLicense.getName().trim());
        if (StringUtils.isNotBlank(artifactLicense.getUrl())) {
          try {
            final URI uri = new URI(artifactLicense.getUrl().trim());
            license.setUrl(uri.toString());
          } catch (URISyntaxException e) {
            // throw it away
          }
        }
        licenseChoice.addLicense(license);
      }
    }
    return licenseChoice;
  }

  private boolean resolveLicenseInfo(final LicenseChoice licenseChoice,
      final LicenseChoice licenseChoiceToResolve, final CycloneDxSchema.Version schemaVersion) {
    if (licenseChoiceToResolve != null) {
      if (licenseChoiceToResolve.getLicenses() != null && !licenseChoiceToResolve.getLicenses()
          .isEmpty()) {
        licenseChoice.addLicense(licenseChoiceToResolve.getLicenses().get(0));
        return true;
      } else if (licenseChoiceToResolve.getExpression() != null
          && CycloneDxSchema.Version.VERSION_10 != schemaVersion) {
        licenseChoice.setExpression(licenseChoiceToResolve.getExpression());
        return true;
      }
    }
    return false;
  }

  @Override
  public Metadata convert(final MavenProject project, CycloneDxSchema.Version schemaVersion) {
    final Tool tool = new Tool();
    final Properties properties = readPluginProperties();
    tool.setVendor(properties.getProperty("vendor"));
    tool.setName(properties.getProperty("name"));
    tool.setVersion(properties.getProperty("version"));
    // Attempt to add hash values from the current mojo
    final Artifact self = new DefaultArtifact(properties.getProperty("groupId"),
        properties.getProperty("artifactId"),
        properties.getProperty("version"), Artifact.SCOPE_COMPILE, "jar", null,
        new DefaultArtifactHandler());
    final Artifact resolved = this.session.getLocalRepository().find(self);
    if (resolved != null) {
      try {
        resolved.setFile(new File(resolved.getFile() + ".jar"));
        tool.setHashes(BomUtils.calculateHashes(resolved.getFile(), schemaVersion));
      } catch (IOException e) {
        this.logger.warn("Unable to calculate hashes of self", e);
      }
    }

    final Component component = new Component();
    component.setGroup(project.getGroupId());
    component.setName(project.getArtifactId());
    component.setVersion(project.getVersion());
    // component.setType(resolveProjectType(projectType));
    //component.setPurl(generatePackageUrl(project.getArtifact()));
    component.setBomRef(component.getPurl());
    extractComponentMetadata(project, component, schemaVersion);

    final Metadata metadata = new Metadata();
    metadata.addTool(tool);
    metadata.setComponent(component);
    return metadata;
  }

  private Properties readPluginProperties() {
    final Properties props = new Properties();
    try {
      props.load(this.getClass().getClassLoader().getResourceAsStream("plugin.properties"));
    } catch (NullPointerException | IOException e) {
      this.logger.warn("Unable to load plugin.properties", e);
    }
    return props;
  }
}
