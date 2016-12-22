import utilities.configuration.ReadYaml
import utilities.job.builder.MavenCiBuilder

final YAML_FILE_CONFIG_PATH = "/var/jenkins_home/job_dsl_script/jenkins_swarm.yaml"

def createBuildJob(projectConfig, branchName) {
    
    def simpleMavenJob = new MavenCiBuilder (
        jobName: "${projectConfig.projectName}-build-${branchName}".replaceAll('/','-'),
        description: 'Simple maven build job',
        numToKeep: 10,
        daysToKeep: 90,
        scmGitUrl: projectConfig.gitConfig.url,
        branchName: branchName,
        credentialKeyId: projectConfig.gitConfig.credentialKeyId
    ).build(this)
    
    return simpleMavenJob.name
}

ReadYaml readYaml = new ReadYaml()
def projectConfigList = readYaml.readJenkinsYaml(YAML_FILE_CONFIG_PATH)
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


