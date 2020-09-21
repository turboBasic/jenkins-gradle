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
                    changed {
                        moduleNotification.sendEmail(currentBuild.result)
                    }
                }
            }
            stage('Publish to Nexus') {
                agent { label 'maven' }
                when {
                    beforeAgent true
                    anyOf {
                        branch 'master'
                        tag 'release-*'
                    }
                }
                steps {
                    script {
                        echo "This is where we publish to Nexus"
                        moduleArtifact.publish()
                        moduleUtils.parseJsonString('{ "aaa": 353453453454354 }')
                    }
                }
            }
            stage 'report', {
                agent { label 'lightweight' }
                when {
                    not {
                        branch 'feature/*'
                    }
                }
                steps {
                    script {
                        moduleUtils.parseJsonString(/{ "result": "${env.BRANCH_NAME}" }/)
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
