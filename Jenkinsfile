pipeline {
  agent any

  tools {
    maven "maven"
  }

  parameters {
    string(name: 'branch', defaultValue: 'master', description: 'The branch to clone or update')
  }

  stages {
    stage('Clone or Update code') {
      steps {
        sh "git pull ${params.branch}:${params.branch}"
      }
    }

    stage('Compile and Clean') {
      steps {
        sh "mvn clean compile"
      }
    }

    stage('Deploy') {
      steps {
        sh "mvn package"
      }
    }

    stage('Archiving') {
      steps {
        archiveArtifacts '**/target/*.jar'
      }
    }
  }
}
