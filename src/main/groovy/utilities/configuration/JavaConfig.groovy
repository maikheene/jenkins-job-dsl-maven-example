package utilities.configuration

/**
 *
 * @author mwolf
 */
class JavaConfig {
    
    final JDK_VERSION_PROPERTY_KEY = "jdk_version"
    
    def Integer javaVersion;
    
    def void readConfigFromList(javaYamlList) {
        javaYamlList.each {
            if (it[JDK_VERSION_PROPERTY_KEY]) {
                javaVersion = it[JDK_VERSION_PROPERTY_KEY].value
            }
        }
    }
}

