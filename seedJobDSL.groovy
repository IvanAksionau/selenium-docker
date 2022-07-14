pipelineJob('pipeline-selenium-tests') {
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
//            scriptPath('jenkins/SeleniumTestPipeline.groovy')
            scriptPath('SeleniumTestPipeline.groovy')
        }
    }
}