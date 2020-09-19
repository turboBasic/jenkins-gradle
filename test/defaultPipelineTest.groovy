import testSupport.PipelineSpockTestBase

class defaultPipelineTest extends PipelineSpockTestBase {

    def script

    def setup() {
        script = loadScript('vars/defaultPipeline.groovy')
    }

    def cleanup() {
        printCallStack()
    }

    void 'Happy flow'() {
        when:
        script.call([:])

        then:
        assertJobStatusSuccess()

    }
}

/*

First off, we create a variable to hold our runnable pipeline (script).
We then see a setup method which is run before each test (feature method).
(Read more about Spock terminology [here](http://spockframework.org/spock/docs/1.3/spock_primer.html))
The cleanup method is run after each test. Finally, there’s the void 'Happy
flow'() {} which is the actual test (or Feature Method as Spock likes to call it).
This test uses the when -> then style of testing, and just calls the pipeline
with a default set of parameters and validates the outcome.

*/
