package utilities.job.builder

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 *
 * @author mwolf
 */
class ShellCiBuilder {
    
    String jobName
    String description
    Integer numToKeep
    Integer daysToKeep
    List<String> scriptsToRun
    
    Job build(DslFactory dslFactory) {
        dslFactory.freeStyleJob(jobName) {  
            logRotator {
                numToKeep = this.numToKeep
                daysToKeep = this.daysToKeep
            }
            blockOnDownstreamProjects()
            steps {
                scriptsToRun.each { script ->
                    shell new File(script).getText('UTF-8')
                }
            }
        }
    }
}

