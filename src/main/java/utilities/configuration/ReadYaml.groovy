package utilities.configuration

import org.yaml.snakeyaml.Yaml
import utilities.configuration.BuildsConfig
import utilities.configuration.GitConfig
import utilities.configuration.JavaConfig
import utilities.configuration.ProjectConfig

/**
 *
 * @author mwolf
 */
class ReadYaml {
    
    private final GIT_CONFIG_YAML_KEY = "git"
    private final JAVA_CONFIG_YAML_KEY = "java"
    private final BUILDS_CONFIG_YAML_KEY = "builds"
    
    def readJenkinsYaml(String fileString) {
        def yamlObject = readYamlObject(fileString)
        def returnList = []
        
        yamlObject.each{
            ProjectConfig projectConfig = new  ProjectConfig();
            projectConfig.projectName = it.key
            projectConfig.gitConfig = readGitConfigFromYaml(it.value)
            projectConfig.javaConfig = readJavaConfigFromYaml(it.value)
            projectConfig.buildsConfig = readBuildsConfigFromYaml(it.value)
            
            returnList << projectConfig
        }
        
        return returnList;
    }
    
    private readYamlObject(String fileString) { 
        String yamleFileContent = new File(fileString).getText('UTF-8')
        
        Yaml yaml = new Yaml()
        def obj = yaml.load(yamleFileContent)
        return obj
    }
    
    private readGitConfigFromYaml(yamlObject) {
        if (yamlObject[GIT_CONFIG_YAML_KEY]) {
            GitConfig gitConfig = new GitConfig()
            gitConfig.readConfigFromList(yamlObject[GIT_CONFIG_YAML_KEY])
            return gitConfig
        }
        
        return null
    }
    
    private readJavaConfigFromYaml(yamlObject) {
        if (yamlObject[JAVA_CONFIG_YAML_KEY]) {
            JavaConfig javaConfig = new JavaConfig()
            javaConfig.readConfigFromList(yamlObject[JAVA_CONFIG_YAML_KEY])
            return javaConfig
        }
        
        return null
    }
    
    private readBuildsConfigFromYaml(yamlObject) {
        if (yamlObject[BUILDS_CONFIG_YAML_KEY]) {
            BuildsConfig buildsConfig = new BuildsConfig()
            buildsConfig.readConfigFromList(yamlObject[BUILDS_CONFIG_YAML_KEY])
            return buildsConfig
        }
        
        return null
    }
}

