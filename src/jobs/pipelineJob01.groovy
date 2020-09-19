
pipeline {
    agent any
    stages {
        stage '1', {
            steps {
                sh 'pwd'
                logError 'Something happened'
                script {
                    moduleUtils.parseJSONString("{foo: 'bar'}")
                }
            }
        }
    }
}
