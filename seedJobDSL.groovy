pipelineJob('pipeline-selenium-tests') {  //https://github.com/jenkinsci/job-dsl-plugin
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/IvanAksionau/selenium-docker.git')
                    }
                    branch('*/master')
                }
            }
            lightweight()
        }
    }
}