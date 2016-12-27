package utilities.configuration

/**
 *
 * @author mwolf
 */
class GitConfig {
    
    final URL_PROPERTY_KEY = "url"
    final BRANCHES_TO_BUILD_PROPERTY_KEY = "branches_to_build"
    final CREDENTIAL_KEY_ID_PROPERTY_KEY = "credential_key_id"
    
    def String url
    def branchesToBuild = []
    def String credentialKeyId
    
    def void readConfigFromList(gitYamlList) {
        gitYamlList.each {
            if (it[URL_PROPERTY_KEY]) {
                url = it[URL_PROPERTY_KEY]
            } else if (it[BRANCHES_TO_BUILD_PROPERTY_KEY]) {
                branchesToBuild = it[BRANCHES_TO_BUILD_PROPERTY_KEY]
            } else if (it[CREDENTIAL_KEY_ID_PROPERTY_KEY]) {
                credentialKeyId = it[CREDENTIAL_KEY_ID_PROPERTY_KEY]
            }
        }
    }
}

