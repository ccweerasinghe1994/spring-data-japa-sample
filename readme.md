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

and we need to create a yml configuration file to specify the parent pom version to be updated:
```yaml
type: specs.openrewrite.org/v1beta/recipe
name: com.yourorg.UpgradeSpringBootParentVersion
displayName: Upgrade Spring Boot Parent Version
recipeList:
  - org.openrewrite.maven.ChangeParentPomVersion:
      groupId: org.springframework.boot
      artifactId: spring-boot-starter-parent
      version: 3.3.2
  - org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3
```

let's write a hook command to add the above plugin to the parent pom:
```bash
#!/bin/sh

# let's run the command in the root of the project

ROOT_DIRECTORY=$(git rev-parse --show-toplevel)
# Define the file to be checked and modified
FILE="$ROOT_DIRECTORY/rewrite.yml"
POM_FILE="$ROOT_DIRECTORY/pom.xml"
# Ensure the mvnw command is executable
chmod +x "$ROOT_DIRECTORY/mvnw"

# check the maven version
"$ROOT_DIRECTORY/mvnw" -v

# let's run the mvn versions:display-dependency-updates

output="$("$ROOT_DIRECTORY/mvnw" versions:display-parent-updates)"
echo "$output"
if echo "$output" | grep -q "The parent project is the latest version"; then
  NOT_UPTO_DATE=false
else
  NOT_UPTO_DATE=true
fi

echo "NOT_UPTO_DATE: $NOT_UPTO_DATE"

# if the parent is not up to date, add the 
# Define the recipe to be added
RECIPE="						<recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3</recipe>"

# Define the recipe to be added
RECIPE_SECTION_TO_ADD=$(cat <<EOF
---
type: specs.openrewrite.org/v1beta/recipe
name: com.yourorg.UpgradeSpringBootParentVersion
displayName: Upgrade Spring Boot Parent Version
recipeList:
  - org.openrewrite.maven.ChangeParentPomVersion:
      groupId: org.springframework.boot
      artifactId: spring-boot-starter-parent
      version: 3.3.2
  - org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3
EOF
)



# Check if the recipe already exists in the file
if ! grep -q "<recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3</recipe>" "$POM_FILE"; then
    # Check the condition
    if [ "$NOT_UPTO_DATE" = true ]; then
        # Add the recipe inside the <activeRecipes> tag
        sed -i '/<activeRecipes>/a\'"$RECIPE"'' pom.xml
        echo "Recipe added to pom.xml"
        if grep -q "name: com.yourorg.UpgradeSpringBootParentVersion" "$FILE"; then
            echo "Recipe already exists in $FILE"
        else
            # Append the recipe to the file
            echo "$RECIPE_SECTION_TO_ADD" >> "$FILE"
            echo "Recipe appended to $FILE"
        fi
    else
        echo "Condition not met, recipe not added"
    fi
else
    echo "Recipe already exists in $FILE"
fi


# Check if the recipe already exists in the file

exit 0
```