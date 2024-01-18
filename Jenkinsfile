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
         sh "git config --global user.email 'lehuuvu110799@gmail.com'" 
        sh "git config --global user.name '
LeHuuVu99'"
         sh "git config pull.rebase false"
        sh "git pull origin ${params.branch}"
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
