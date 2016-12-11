package utilities.configuration

/**
 *
 * @author mwolf
 */
class BuildsConfig {
    
    final KEEP_BUILDS_PROPERTY_KEY = "keep_builds"
    final KEEP_DAYS_PROPERTY_KEY = "keep_day"
    
    def Integer keepBuilds
    def Integer keepDays
    
    def void readConfigFromList(buildYamlList) {
        buildYamlList.each {
            if (it[KEEP_BUILDS_PROPERTY_KEY]) {
                keepBuilds = it[KEEP_BUILDS_PROPERTY_KEY].value
            } else if (it[KEEP_DAYS_PROPERTY_KEY]) {
                keepDays = it[KEEP_DAYS_PROPERTY_KEY].value
            }
        }
    }
}

