import testSupport.PipelineSpockTestBase
import moduleArtifact
import moduleNotification
import moduleUtils

class defaultPipelineTest extends PipelineSpockTestBase {

    def script
    def mavenMock
    def artifactMock
    def notificationMock
    def utilsMock

    def registerMocks() {
        mavenMock = Mock(Closure)
        helper.registerAllowedMethod('moduleMaven', [String.class], mavenMock)

        artifactMock = Mock(moduleArtifact)
        binding.setVariable('moduleArtifact', artifactMock)

        notificationMock = Mock(moduleNotification)
        binding.setVariable('moduleNotification', notificationMock)

        utilsMock = Mock(moduleUtils)
        binding.setVariable('moduleUtils', utilsMock)
    }

    def registerPluginMethods() {
        // Junit
        // https://plugins.jenkins.io/junit
        helper.registerAllowedMethod('junit', [HashMap.class], null)
    }

    def setup() {
        registerMocks()
        registerPluginMethods()
        script = loadScript('vars/defaultPipeline.groovy')
    }

    def cleanup() {
        printCallStack()
    }

    void '01. Happy flow'() {
        given:
        def junitMock = Mock(Closure)
        helper.registerAllowedMethod('junit', [HashMap.class], junitMock)

        when:
        script.call([:])

        then:
        1 * mavenMock.call('clean verify')
        1 * junitMock.call(_)
        // 1 * artifactMock.publish()
        1 * notificationMock.sendEmail(_)
        // 2 * utilsMock.parseJsonString(_)
        assertJobStatusSuccess()
    }

    void '02. Rainy day'() {
        given:
        def junitMock = Mock(Closure)
        helper.registerAllowedMethod('junit', [HashMap.class], junitMock)

        when:
        script.call([:])

        then:
        1 * mavenMock.call(_) >> {
            binding.getVariable('currentBuild').result = 'FAILURE'
        }
        1 * junitMock.call(_)
        0 * artifactMock.publish()
        2 * notificationMock.sendEmail(_)
        assertJobStatusFailure()
    }

    void '03. A maven failure should still interpret the junit test report'() {
        given:
        def junitMock = Mock(Closure)
        helper.registerAllowedMethod('junit', [HashMap.class], junitMock)

        when:
        script.call([:])

        then:
        1 * mavenMock.call('clean verify') >> {
            binding.getVariable('currentBuild').result = 'FAILURE'
        }
        1 * junitMock.call(_)
        assertJobStatusFailure()
    }

    void '04. Send notification when status of maven call changes'() {
        given:
        def junitMock = Mock(Closure)
        helper.registerAllowedMethod('junit', [HashMap.class], junitMock)

        and:
        binding.getVariable('currentBuild').previousBuild.result = 'FAILED'

        when:
        script.call([:])

        then:
        1 * mavenMock.call('clean verify')
        1 * junitMock.call(_)
        2 * notificationMock.sendEmail(_)
        assertJobStatusSuccess()
    }

    void '05. When not on master, do not publish to Nexus'() {
        given:
        binding.setVariable('BRANCH_NAME', 'develop')

        when:
        script.call([:])

        then:
        1 * mavenMock.call('clean verify')
        0 * artifactMock.publish()
        assertJobStatusSuccess()
    }

    void '06. When building a release tag, publish to Nexus'() {
        given:
        binding.setVariable('TAG_NAME', 'release-1.1.0')

        when:
        script.call([:])

        then:
        1 * mavenMock.call('clean verify')
        1 * artifactMock.publish()
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
