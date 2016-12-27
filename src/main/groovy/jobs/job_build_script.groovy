
import utilities.configuration.ReadYaml
import utilities.job.builder.MavenCiBuilder
import utilities.job.builder.ShellCiBuilder

final YAML_FILE_CONFIG_PATH = "/var/jenkins_home/job_dsl_script/jenkins_swarm.yaml"
final BASE_PATH = "createdJobs"

def createBuildJobs(projectConfig, basePath, branchName) {
    new MavenCiBuilder (
        jobName: "${basePath}/${projectConfig.projectName}-build-${branchName}",
        description: 'Simple maven build job',
        numToKeep: 10,
        daysToKeep: 90,
        scmGitUrl: projectConfig.gitConfig.url,
        branchName: branchName,
        credentialKeyId: projectConfig.gitConfig.credentialKeyId
    ).build(this)
    
    new ShellCiBuilder(
        jobName: "${basePath}/${projectConfig.projectName}-script-${branchName}",
        description: 'Simple script build job',
        numToKeep: 10,
        daysToKeep: 90,
        scriptsToRun: ["${WORKSPACE}/src/main/resources/test1.sh"
            , "${WORKSPACE}/src/main/resources/test2.sh"]
    ).build(this)
}

ReadYaml readYaml = new ReadYaml()
def projectConfigList = readYaml.readJenkinsYaml(YAML_FILE_CONFIG_PATH)

folder(BASE_PATH) {
    description 'All jobs that are created with the seed job'
}

projectConfigList.each { projectConfig ->
    def projectBasePath = "${BASE_PATH}/${projectConfig.projectName}"
    folder(projectBasePath) {
        description 'All branch pipelines'
    }
    projectConfig.gitConfig.branchesToBuild.each { branchName ->
        def branchPath = "${projectBasePath}/${branchName}"
        folder(branchPath) {
            description 'All jobs for the pipeline'
        }
        def buildJobNames = createBuildJobs(
            projectConfig, branchPath, branchName)
    }
}


