![maven workflow](https://github.com/enofex/naikan-maven-plugin/actions/workflows/maven.yml/badge.svg) [![](https://img.shields.io/badge/Java%20Version-17-orange)](/pom.xml)
<img height="20" src="https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg">
# The naikan-maven-plugin module 

This module provides a maven plugin which enrich the Naikan BOM with information of the pom.xml.

Maven Usage
-------------------

```xml
<!-- uses default configuration -->
<plugin>
    <groupId>com.enofex</groupId>
    <artifactId>naikan-maven-plugin</artifactId>
    <version>0.1.0</version>
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

## Contributing
If you want to contribute to this project, then follow please these [instructions](https://github.com/enofex/naikan/blob/main/CONTRIBUTING.md).

## Website
Visit the [Naikan](https://naikan.io) Website for general information, demos and documentation.
