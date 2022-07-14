pipelineJob('pipeline-selenium-tests') {  //https://github.com/jenkinsci/job-dsl-plugin
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github('IvanAksionau/selenium-docker')
                    }
                    branch('*/master')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}