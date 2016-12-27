package utilities.job.builder

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.jobs.MavenJob
import org.junit.*
import static groovy.test.GroovyAssert.*

/**
 *
 * @author mwolf
 */
class MavenCiBuilderTest {
    MavenCiBuilder builder
    JobParent jobParent = JobParentBuilder.createJobParent()
    
    String jobName = "TestJob"
    String description = "Test description"
    Integer numToKeep = 10
    Integer daysToKeep = 90
    String scmGitUrl = "git@github.com:maikwolf/jenkins-job-dsl-maven-example.git"
    String branchName = "master"
    String credentialKeyId = "1321ndsajd13123"
    
    @Before
    public void setUp() {
        builder = new MavenCiBuilder (
            jobName: this.jobName,
            description: this.description,
            numToKeep: this.numToKeep,
            daysToKeep: this.daysToKeep,
            scmGitUrl: this.scmGitUrl,
            branchName: this.branchName,
            credentialKeyId: this.credentialKeyId
        )
    }
    
    @Test
    void textXmlBasicJobConfiguration() {
        Job job = builder.build(jobParent)
        def node = job.node
        
        //Check job type
        assertTrue(job instanceof MavenJob)
        
        //Check job name
        assertEquals(job.name, this.jobName)
        
        //Check description
        assertEquals(node.description.text(), this.description)
        
        //Check log rotate configuration
        assertEquals(node.logRotator.daysToKeep.text(), 
            this.daysToKeep.toString())
        assertEquals(node.logRotator.numToKeep.text(), 
            this.numToKeep.toString())
        
        def scmGit = node.scm
        //Check git remote configuration
        def gitRemote = scmGit.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'
        assertEquals(gitRemote.name.text(), "origin")
        assertEquals(gitRemote.url.text(), this.scmGitUrl)
        assertEquals(gitRemote.credentialsId.text(), this.credentialKeyId)
        
        //Check git branch
        def branch = scmGit.branches.'hudson.plugins.git.BranchSpec'
        assertEquals(branch.name.text(), this.branchName)
        
        //Check if blockOnDownstreamProject exits
        assertNotNull(node.blockOnDownstreamProjects)
        
        //Check maven goals
        assertEquals(node.goals.text(), 'clean package')
    }
    
    @Test
    void testXmlOfShellCiBuilderWithOverwriteValues() {
        builder.jobName = "Overwrite JobName"
        builder.branchName = "feature/branch"
        builder.credentialKeyId = "asdasdf1234123"
        
        Job job = builder.build(jobParent)
        def node = job.node
        
        //Check job name
        assertEquals(job.name, "Overwrite JobName")
        
        def scmGit = node.scm
        //Check git remote configuration
        def gitRemote = scmGit.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'
        assertEquals(gitRemote.credentialsId.text(), "asdasdf1234123")
        
        //Check git branch
        def branch = scmGit.branches.'hudson.plugins.git.BranchSpec'
        assertEquals(branch.name.text(), "feature/branch")
    }
}

