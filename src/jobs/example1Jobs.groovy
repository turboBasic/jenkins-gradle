String basePath = 'example1'
String repo = 'sheehan/gradle-example'

// job("$basePath/gradle-example-build") {
//     scm {
//         github repo
//     }
//     triggers {
//         scm 'H/5 * * * *'
//     }
//     steps {
//         gradle 'assemble'
//     }
// }

folder('example1') {
    description 'Created by jenkins-gradle'
}

job("$basePath/gradle-example-deploy") {
    parameters {
        stringParam 'host'
    }
    steps {
        shell 'scp war file; restart...'
    }
}
