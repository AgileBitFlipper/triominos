pipeline {

    // Run on any executor.
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }

    stages {

        stage('Build') {

            steps {
                echo 'Building...'
                sh 'mvn -B -V clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing'
                sh 'mvn -B -V verify'
                junit testResults: '**/target/*-reports/TEST-*.xml'
            }
        }

        stage('JaCoCo') {
            steps {
                echo 'Code Coverage'
                jacoco()
            }
        }

        stage('Sonar') {
            steps {
                echo 'Sonar Scanner'
               	//def scannerHome = tool 'SonarQube Scanner 3.0'
			    // withSonarQubeEnv('SonarQube Server') {
			    // 	sh 'C:/Dock/ci/sonar/sonar-scanner-3.0.3.778-windows/bin/sonar-scanner'
			    // }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging'
                sh 'mvn -B -V -P package-only package'
            }
        }

        stage('Analysis') {

            steps {

                echo 'Analyzing...'

                script {
                    sh "mvn -B -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs findbugs:findbugs"
        
                    def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
                    publishIssues issues: [checkstyle]
            
                    def pmd = scanForIssues tool: pmdParser(pattern: '**/target/pmd.xml')
                    publishIssues issues: [pmd]
                    
                    def cpd = scanForIssues tool: cpd(pattern: '**/target/cpd.xml')
                    publishIssues issues: [cpd]
                    
                    def spotbugs = scanForIssues tool: [$class: 'SpotBugs'], pattern: '**/target/spotbugsXml.xml')
                    publishIssues issues: [spotbugs]

                    def findbugs = scanForIssues tool: [$class: 'FindBugs'], pattern: '**/target/findbugsXml.xml'
                    publishIssues issues:[findbugs]

                    def maven = scanForIssues tool: mavenConsole()
                    publishIssues issues: [maven]
                    
                    def java = scanForIssues tool: [$class: 'Java']
                    publishIssues issues: [java]

                    def javadoc = scanForIssues tool: [$class: 'JavaDoc']
                    publishIssues issues: [javadoc]

                    publishIssues id: 'gatherJava', name: 'Java and JavaDoc',
                        issues: [java, javadoc], 
                        filters: [includePackage('io.jenkins.plugins.analysis.*')]

                    publishIssues id: 'gatherAnalysis', name: 'All Issues', 
                        issues: [checkstyle, pmd, cpd, spotbugs, maven]
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '## TODO DEPLOYMENT ##'
            }
        }
    }

    post {
        always {
            echo 'JENKINS PIPELINE'
            archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
            // step([
            //   $class         : 'FindBugsPublisher',
            //   pattern        : 'build/reports/findbugs/*.xml',
            //   canRunOnFailed : true
            // ])

            // step([
            //   $class         : 'PmdPublisher',
            //   pattern        : 'build/reports/pmd/*.xml',
            //   canRunOnFailed : true
            // ])
            // step([
            //   $class           : 'JacocoPublisher',
            //   execPattern      : 'build/jacoco/jacoco.exec',
            //   classPattern     : 'target/classes',
            //   sourcePattern    : 'src/main/java',
            //   exclusionPattern : '**/*Test.class'
            // ])

            // publishHTML([
            //   allowMissing          : false,
            //   alwaysLinkToLastBuild : false,
            //   keepAll               : true,
            //   reportDir             : 'target/site',
            //   reportFiles           : 'checkstyle.html',
            //   reportTitles          : "CheckStyle",
            //   reportName            : "CheckStyle"
            // ])
        }
        success {
            echo 'JENKINS PIPELINE SUCCESSFUL'
        }
        failure {
            echo 'JENKINS PIPELINE FAILED'
            echo 'mail to: andrew.montcrieff@cesicorp.com, subject: "The Pipeline failed :("'
        }
        unstable {
            echo 'JENKINS PIPELINE WAS MARKED AS UNSTABLE'
        }
        changed {
            echo 'JENKINS PIPELINE STATUS HAS CHANGED SINCE LAST EXECUTION'
        }
    }
}