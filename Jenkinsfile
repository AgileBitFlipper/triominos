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
            // When you add testing, uncomment this line to collect the surefire-reports
            junit 'target/surefire-reports/*.xml'
            step([
              $class         : 'FindBugsPublisher',
              pattern        : 'build/reports/findbugs/*.xml',
              canRunOnFailed : true
            ])
            step([
              $class         : 'PmdPublisher',
              pattern        : 'build/reports/pmd/*.xml',
              canRunOnFailed : true
            ])
            step([
              $class           : 'JacocoPublisher',
              execPattern      : 'build/jacoco/jacoco.exec',
              classPattern     : 'build/classes/main',
              sourcePattern    : 'src/main/java',
              exclusionPattern : '**/*Test.class'
            ])
            publishHTML([
              allowMissing          : false,
              alwaysLinkToLastBuild : false,
              keepAll               : true,
              reportDir             : 'build/asciidoc/html5',
              reportFiles           : 'index.html',
              reportTitles          : "API Documentation",
              reportName            : "API Documentation"
            ])
        }
    }

    stages {
        stage ('Checkout') {
            git branch:'5.0', url: 'git@github.com:jenkinsci/warnings-plugin.git'
        }
        // While there's only one stage here, you can specify as many stages as you like!
        stage('Build') {

            junit testResults: '**/target/*-reports/TEST-*.xml'

            def java = scanForIssues tool: [$class: 'Java']
            def javadoc = scanForIssues tool: [$class: 'JavaDoc']
         
            publishIssues issues: [java, javadoc], filters: [includePackage('io.jenkins.plugins.analysis.*')]

            steps {
                sh 'mvn -B clean compile verify package'
            }
        }
        stage('Analysis') {
            def mvnHome = tool 'mvn-default'
 
            sh "${mvnHome}/bin/mvn -batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd findbugs:findbugs spotbugs:spotbugs"
 
            def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
            publishIssues issues: [checkstyle]
    
            def pmd = scanForIssues tool: pmdParser(pattern: '**/target/pmd.xml')
            publishIssues issues: [pmd]
            
            def cpd = scanForIssues tool: cpd(pattern: '**/target/cpd.xml')
            publishIssues issues: [cpd]
            
            def spotbugs = scanForIssues tool: spotBugs(pattern: '**/target/findbugsXml.xml')
            publishIssues issues: [spotbugs]

            def maven = scanForIssues tool: mavenConsole()
            publishIssues issues: [maven]
            
            publishIssues id: 'analysis', name: 'All Issues', 
                issues: [checkstyle, pmd, spotbugs], 
                filters: [includePackage('io.jenkins.plugins.analysis.*')]
        }
    }

}