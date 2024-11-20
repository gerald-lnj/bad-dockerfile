pipeline {
    agent any
    parameters {
        string(
            name: 'TAR_PATH',
            description: 'Path to the image archive relative to workspace',
            defaultValue: 'vuln-image.tar'
        )
        choice(name: 'REPORT_FORMAT', choices: ['JSON', 'HTML', 'SARIF'], description: 'Report format')
        credentials(
            name: 'BITDETECTOR_LICENSE',
            description: 'Bitdetector license',
            credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl',
            required: true
        )
        credentials(
            name: 'GET_BITDETECTOR_RELEASE_CREDENTIAL',
            description: ' A Jenkins credential that contaains the PAT to fetch Bitdetector releases',
            credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl',
            required: true
        )
    }

    stages {
        stage('Generate Docker Image archive') {
            steps {
                sh """
                docker build -t debug .
                docker save -o ${WORKSPACE}/${params.TAR_PATH} debug
                tree .
                """
            }
        }
        stage('Scan') {
            environment  {
                GH_TOKEN = credentials('GET_BITDETECTOR_RELEASE_CREDENTIAL')
                BITDETECTOR_LICENSE = credentials('BITDETECTOR_LICENSE')
            }
            steps {
                script {
                    scan = load "scan.groovy"
                    scan.bitdetectorScanImage(
                        "${WORKSPACE}/${params.TAR_PATH}",
                        params.REPORT_FORMAT,
                        "report.${params.REPORT_FORMAT.toLowerCase()}",
                        "${env.JOB_BASE_NAME}-${env.BUILD_NUMBER}",
                    )
                }
            }
        }
    }
}
