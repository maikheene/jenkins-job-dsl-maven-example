import utilities.configuration.ReadYaml

def createBuildJob(projectConfig, branchName) {
    def jobName = "${projectConfig.projectName}-build-${branchName}".replaceAll('/','-')
    mavenJob(jobName) {
        scm {
            git {
                remote {
                    name('origin')
                    url(projectConfig.gitConfig.url)
                    credentials(projectConfig.gitConfig.credentialKeyId)
                }
                branch(branchName)
            }
        }
        blockOnDownstreamProjects()
        goals('clean package')
    }
    
    return jobName
}

ReadYaml readYaml = new ReadYaml()
def projectConfigList = readYaml.readJenkinsYaml("/var/jenkins_home/job_dsl_script/jenkins_swarm.yaml")
def createdJobNames = [];

projectConfigList.each {
    
    def projectConfig = it
    projectConfig.gitConfig.branchesToBuild.each {
        def branchName = it
        def buildJobName = createBuildJob(projectConfig, branchName)
        createdJobNames << buildJobName
        
        
    }
}

listView('Build-Job-View') {
    jobs { 
        createdJobNames.each {
            name(it)
        }
    }
    columns {
	status()
	weather()
	name()
	lastSuccess()
	lastFailure()
	lastDuration()
	buildButton()
    }
}


