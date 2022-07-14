pipelineJob('pipeline-selenium-tests') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github('IvanAksionau/selenium-docker')
                    }
                }
            }
//            scriptPath('jenkins/SeleniumTestPipeline.groovy')
            scriptPath('SeleniumTestPipeline.groovy')
        }
    }
}