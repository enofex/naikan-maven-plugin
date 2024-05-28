![maven workflow](https://github.com/enofex/naikan-maven-plugin/actions/workflows/maven.yml/badge.svg) [![](https://img.shields.io/badge/Java%20Version-21-orange)](/pom.xml)
<img height="20" src="https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg">
# The naikan-maven-plugin module 

The Naikan Maven Plugin is a tool designed to streamline and enhance the build and management process of Maven-based projects. This plugin facilitates the integration of project information from a Maven POM (Project Object Model) into the Naikan Bill of Materials (BOM), all while offering flexible customization through a variety of providers.

At its core, the Naikan Maven Plugin serves as a bridge between your Maven project and the Naikan BOM. The plugin empowers developers to efficiently extract project metadata, such as versions, and other essential details, from the POM file. This extracted information is then seamlessly integrated into the Naikan BOM.

One of the standout features of the Naikan Maven Plugin is its support for custom providers. Developers have the freedom to define their own data providers, allowing for tailored integration. This extensibility enables the plugin to adapt to the unique needs and structures of different projects. Additionally, the plugin offers the capability to fine-tune the order in which these custom providers are employed. 

To further enhance the customization potential, the Naikan Maven Plugin supports the use of a custom `naikan.json` file. This JSON file can be added to your project, providing a centralized way to define additional information that should be merged into the Naikan BOM. 

Maven Usage
-------------------

```xml
<!-- uses default configuration -->
<plugin>
    <groupId>com.enofex</groupId>
    <artifactId>naikan-maven-plugin</artifactId>
    <version>1.0.2</version>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>aggregate</goal>
        </goals>
      </execution>
    </executions>
</plugin>
```

Default Values
-------------------
```xml
<plugins>
    <plugin>
      <groupId>com.enofex</groupId>
      <artifactId>naikan-maven-plugin</artifactId>
      <configuration>
        <inputFileName>naikan.json</inputFileName>
        <inputDirectory>${project.basedir}</inputDirectory>
        <outputFileName>naikan.json</outputFileName>
        <outputDirectory>${project.build.directory}</outputDirectory>
        <skip>false</skip><!-- ${naikan.skip} -->
      </configuration>
    </plugin>
</plugins>
```

Goals
-------------------
The Naikan Maven plugin contains the following goals:
* `aggregate`: creates a Naikan BOM for each Maven module and a Naikan BOM at parents root

JVM Parameter
-------------------
* `-Dnaikan.skip=true`: skips the plugin
* `-Dnaikan.json.prettyPrint=true`: json file output should be pretty printed
* `-Dnaikan.commits.last=3m`: specifies the time unit since the commits should be taken (2d,3w,5m,1y)

## Contributing
If you want to contribute to this project, then follow please these [instructions](https://github.com/enofex/naikan/blob/main/CONTRIBUTING.md).

## Website
Visit the [Naikan](https://naikan.io) Website for general information, demos and documentation.
