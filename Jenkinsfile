pipeline {
    // Make sure that the tools we need are installed and on the path.
    //tools {
        //maven "Maven 3.6.1"
        //jdk "Oracle JDK 8u40"
    //}

    // Run on any executor.
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }

    // The order that sections are specified doesn't matter - this will still be run
    // after the stages, even though it's specified before the stages.
    post {
        // No matter what the build status is, run these steps. There are other conditions
        // available as well, such as "success", "failed", "unstable", and "changed".
        always {
            archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
    }

    stages {
        // While there's only one stage here, you can specify as many stages as you like!
        stage('Build') { 
            steps {
                sh 'mvn -B clean package'
                //sh 'mvn -B -DskipTests clean package' 
            }
        }
    }

}