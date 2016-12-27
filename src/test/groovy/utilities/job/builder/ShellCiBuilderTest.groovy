package utilities.job.builder

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.jobs.FreeStyleJob
import org.junit.*
import static groovy.test.GroovyAssert.*

/**
 *
 * @author mwolf
 */
class ShellCiBuilderTest {
    
    ShellCiBuilder builder
    JobParent jobParent = JobParentBuilder.createJobParent()
    
    String jobName = "TestJob"
    String description = "Test description"
    Integer numToKeep = 10
    Integer daysToKeep = 90
    
    @Before
    public void setUp() {
        builder = new ShellCiBuilder (
            jobName: this.jobName,
            description: this.description,
            numToKeep: this.numToKeep,
            daysToKeep: this.daysToKeep
        )
    }
    
    @After
    public void tearDown() {}
    
    @Test
    void textXmlBasicJobConfiguration() {
        Job job = builder.build(jobParent)
        def node = job.node
        
        //CheckJob
        assertEquals(job.name, this.jobName)
        
        //Check description
        assertEquals(node.description.text(), this.description)
        
        //Check log rotate
        assertEquals(node.logRotator.daysToKeep.text(), 
            this.daysToKeep.toString())
        assertEquals(node.logRotator.numToKeep.text(), 
            this.numToKeep.toString())
        
        //Check if blockOnDownstreamProject exits
        assertNotNull(node.blockOnDownstreamProjects)
    }
    
    @Test
    void testXmlOfShellCiBuilderWithOverwriteValues() {
        builder.jobName = "OverwriteJobName"
        builder.numToKeep = 20
        builder.daysToKeep = 120
        
        Job job = builder.build(jobParent)
        def node = job.node
        
        //Check job type
        assertTrue(job instanceof FreeStyleJob)
        
        //CheckJob
        assertEquals(job.name, "OverwriteJobName")
        
        //Check log rotate
        assertEquals(node.logRotator.daysToKeep.text(), 
            "120")
        assertEquals(node.logRotator.numToKeep.text(), 
            "20")
    }
}

