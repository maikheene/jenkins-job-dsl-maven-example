
import utilities.configuration.ReadYaml
import utilities.job.builder.MavenCiBuilder
import utilities.job.builder.ShellCiBuilder

final YAML_FILE_CONFIG_PATH = "/var/jenkins_home/job_dsl_script/jenkins_swarm.yaml"

def createBuildJobs(projectConfig, branchName) {
    
    def jobNames = []
    
    def simpleMavenJob = new MavenCiBuilder (
        jobName: "${projectConfig.projectName}-build-${branchName}".replaceAll('/','-'),
        description: 'Simple maven build job',
        numToKeep: 10,
        daysToKeep: 90,
        scmGitUrl: projectConfig.gitConfig.url,
        branchName: branchName,
        credentialKeyId: projectConfig.gitConfig.credentialKeyId
    ).build(this)
    jobNames << simpleMavenJob.name.toString()
    
    def simpleShellJob = new ShellCiBuilder(
        jobName: "${projectConfig.projectName}-script-${branchName}".replaceAll('/','-'),
        description: 'Simple script build job',
        numToKeep: 10,
        daysToKeep: 90,
        scriptsToRun: ["${WORKSPACE}/src/main/resources/test1.sh"
        , "${WORKSPACE}/src/main/resources/test2.sh"]
    ).build(this)
    jobNames << simpleShellJob.name.toString()
    
    return jobNames
}

ReadYaml readYaml = new ReadYaml()
def projectConfigList = readYaml.readJenkinsYaml(YAML_FILE_CONFIG_PATH)
def createdJobNames = [];

projectConfigList.each {
    
    def projectConfig = it
    projectConfig.gitConfig.branchesToBuild.each {
        def branchName = it
        def buildJobNames = createBuildJobs(projectConfig, branchName)
        createdJobNames.addAll(buildJobNames)
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


