def call(Map pipelineParams) {
    pipeline {
        agent none

        stages {
            stage('Build and Unit test') {
                agent { label 'maven' }
                steps {
                    script {
                        moduleMaven('clean verify')
                    }
                }
                post {
                    always {
                        junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: false
                    }
                }
            }
            stage('Publish to Nexus') {
                agent { label 'maven' }
                steps {
                    script {
                        echo "This is where we publish to Nexus"
                        moduleArtifact.publish()
                        moduleUtils.parseJsonString('{ "aaa": 353453453454354 }')
                    }
                }
            }
        }
        post {
            always {
                script {
                    moduleNotification.sendEmail(currentBuild.result)
                    echo moduleUtils.parseJsonString('{ "aaa": 353453453454354, "zzz": [{"id": "444"}, {"id": "555"}] }')
                }
            }
        }
    }
}
