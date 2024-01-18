pipeline {
    agent any
 tools {
        maven "maven"
    }
    stages {
        stage('clone code') {
            steps {
                sh "git clone origin ${params.branch}"
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
        
        // stage('Push code') {
        //     steps {
        //         sh "git push origin ${params.branch}"
        //     }
        // }
        
        stage('Archiving') {
            steps {
                archiveArtifacts '**/target/*.jar'
            }
        }
    }
}
