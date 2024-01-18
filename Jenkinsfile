pipeline {
    agent any
    
    tools {
        maven "maven"
    }
    
    // parameters {
    //     string(name: 'branch', defaultValue: 'master', description: 'The branch to clone or update')
    // }
    
    stages {
        stage('Clone or Update code') {
            steps {
                 sh "git clone ${params.branch} .
                // script {
                //     def gitUrl = "https://github.com/jukibach/university_management.git"
                //     def workspaceDir = pwd()
                    
                //     if (!fileExists("${workspaceDir}/.git")) {
                //         // Clone repository if it doesn't exist in the workspace
                //         sh "git clone ${gitUrl} ."
                //     } else {
                //         // Update repository if it already exists in the workspace
                //         dir(workspaceDir) {
                //             sh "git checkout ${params.branch}"
                //             sh "git pull origin ${params.branch}"
                //         }
                //     }
                // }
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
