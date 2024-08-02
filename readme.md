```xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>versions-maven-plugin</artifactId>
                <version>2.17.1</version>
                <configuration>
                    <ruleSet>
                        <!-- zero or more elements -->
                        <ignoreVersions>
                            <!-- zero or more elements -->
                            <ignoreVersion>
                                <version>1.0.0</version>
                            </ignoreVersion>
                            <ignoreVersion>
                                <!-- can be either: 'exact' (default), 'regex' or 'range' -->
                                <type>regex</type>
                                <version>(.+-SNAPSHOT|.+-M\d)</version>
                            </ignoreVersion>
                            <ignoreVersion>
                                <type>regex</type>
                                <version>.+-(alpha|beta)</version>
                            </ignoreVersion>
                        </ignoreVersions>
                    </ruleSet>
                    <allowMajorUpdates>false</allowMajorUpdates> <!-- Add this line -->
                    <allowSnapshots>false</allowSnapshots> <!-- Add this line -->
                </configuration>
            </plugin>
```

we can use the report goal to see parent pom versions that are not up to date:
```bash
 <reporting>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>versions-maven-plugin</artifactId>
                <version>2.17.1</version>
                <configuration>
                   <formats>
                        <format>xml</format>
                    </formats>
                </configuration>
                <reportSets>
                    <reportSet>
                        <reports>
                            <report>parent-updates-report</report>
                        </reports>
                    </reportSet>
                </reportSets>
            </plugin>
        </plugins>
    </reporting>
```

how to use maven versions plugin to decide whether a parent pom version is available or not:
we can use a git hook to run the versions plugin and check if the parent pom version is available or not.
```bash
#!/bin/sh

# let's run the command in the root of the project

ROOT_DIRECTORY=$(git rev-parse --show-toplevel)

# Ensure the mvnw command is executable
chmod +x "$ROOT_DIRECTORY/mvnw"

# check the maven version
"$ROOT_DIRECTORY/mvnw" -v

# let's run the mvn versions:display-dependency-updates

output="$("$ROOT_DIRECTORY/mvnw" versions:display-parent-updates)"
if echo "$output" | grep -q "The parent project is the latest version"; then
  UPTO_DATE=true
else
  UPTO_DATE=false
fi

echo "UPTO_DATE: $UPTO_DATE"

exit 0

```

to run the above maven command we need to add the plugin below 

```xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>versions-maven-plugin</artifactId>
                <version>2.17.1</version>
                <configuration>
                    <ruleSet>
                        <!-- zero or more elements -->
                        <ignoreVersions>
                            <ignoreVersion>
                                <!-- can be either: 'exact' (default), 'regex' or 'range' -->
                                <type>regex</type>
                                <version>(.+-SNAPSHOT|.+-M\d)</version>
                            </ignoreVersion>
                            <ignoreVersion>
                                <type>regex</type>
                                <version>.+-(alpha|beta)</version>
                            </ignoreVersion>
                        </ignoreVersions>
                    </ruleSet>
                    <allowMajorUpdates>false</allowMajorUpdates> <!-- Add this line -->
                    <allowSnapshots>false</allowSnapshots> <!-- Add this line -->
                </configuration>
            </plugin>
```

if we have a parent pom version available we can use openrewrite to update the parent pom version:
for a spring boot project we can use the below command to update the parent pom version:
```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.37.0</version>
    <configuration>
        <exportDatatables>true</exportDatatables>
        <scope>compile</scope>
        <overrideTransitive>true</overrideTransitive>
        <addMarkers>true</addMarkers>
        <activeRecipes>
            <recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3</recipe>
            <recipe>org.openrewrite.java.dependencies.DependencyVulnerabilityCheck</recipe>
        </activeRecipes>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-java-dependencies</artifactId>
            <version>1.14.0</version>
        </dependency>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-spring</artifactId>
            <version>5.16.0</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</plugin>
```
