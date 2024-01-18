pipeline {
    agent any
 tools {
        maven "maven"
    }
     stages {
        stage('Clone or Update code') {
            steps {
                script {
                    def gitUrl = "https://github.com/jukibach/university_management.git"
                    def workspaceDir = pwd()
                    
                    if (!fileExists("${workspaceDir}/.git")) {
                        // Clone repository if it doesn't exist in the workspace
                        sh "git clone ${gitUrl} ."
                    } else {
                        // Update repository if it already exists in the workspace
                        sh "git pull"
                    }
                }
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
