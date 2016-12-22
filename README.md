# Create a jenkins job dsl with a yaml configuration

## Create a seed job

- Create a new **Freestyle** project
- In the **Source Code Management** select the git option
  - Add the **_Repository URL_** and selected your credentials
- In the **Build** section, add the build step **Process Job DSL**
  - Select the option **Look on File system**
  - Add your filename to the **DSL Scripts** field
    - For this example enter **_src/main/java/jobs/\*\*/*.groovy_**
  - Open **Advanced** section and put **_src/main/java_** and 
  **_src/main/lib/*.jar_** to the **Additional classpath** field in 
  spepareated rows. (Imports the snakeYaml libary and other classes)
- Save
   
## Yaml configuration file

In the **_job_buildscript.grovy_** file you find the constant:

```groovy
final YAML_FILE_CONFIG_PATH = "/your/path/to/the/yaml/file"
```

Here is an example for the current configuration:
```
ProjectName:
  git:
    - url: {git url}
    - branches_to_build: 
      - develop
      - release
      - master
    - credential_key_id: {credential_id_from_jenkins}
  java: 
    - jdk_version: 8
  builds:
    - keep_builds: 5
    - keep_day: 90
```

