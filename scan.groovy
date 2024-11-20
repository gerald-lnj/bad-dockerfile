#!/usr/bin/env groovy

def bitdetectorScanImage(
    String imageAbsPath,
    String reportFormat,
    String reportFilename = '',
    String reportName = ''
) {
    assert reportFormat in ['JSON', 'HTML', 'SARIF']
    if (reportFilename == '') {
        reportFilename = "report.${reportFormat.toLowerCase()}"
    }
    echo 'Setting up directory structure'
    sh 'mkdir bitdetector'
    dir('bitdetector') {
        sh """
        gh release -R horangi-ir/bitdetector download -p bitdetector_Linux_x86_64.tar.gz
        tar -xzf bitdetector_Linux_x86_64.tar.gz
        tar -xzf dependencies/ctc.tar.gz -C .
        ./bitdetector filesystem \"${imageAbsPath}\" -f ${reportFormat} -p \"${reportFilename}\" -n \"${reportName}\"
        cat ${reportFilename}
        """
    }
    return
}

return this
