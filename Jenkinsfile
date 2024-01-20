pipeline {

    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        POSTGRESQL_ROOT_LOGIN = credentials('postgres')
    }
    stages {

        stage('Build with Maven') {
            steps {
                sh 'docker --version'
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Packaging/Pushing image') {

            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'sudo docker build -t jukibach/springboot .'
                    sh 'sudo docker push jukibach/springboot'
                }
            }
        }

        stage('Deploy PostgreSQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'sudo docker image pull postgres'
                sh 'sudo docker network create dev || echo "this network exists"'
                sh 'sudo docker container stop jukibach-postgres || echo "this container does not exist" '
                sh 'echo y | docker container prune '
                sh 'sudo docker volume rm jukibach-postgres-data || echo "no volume"'

                sh "sudo docker run --name jukibach-postgres --rm --network dev -v jukibach-postgres-data:/var/lib/postgres -e POSTGRESQL_PASSWORD=${POSTGRESQL_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=university_management -d postgresql "
                sh 'sleep 20'
                sh "sudo docker exec -i jukibach-postgres postgresql --user=postgres --password=${MYSQL_ROOT_LOGIN_PSW} < script"
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'sudo docker image pull jukibach/springboot'
                sh 'sudo docker container stop jukibach-springboot || echo "this container does not exist" '
                sh 'sudo docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '

                sh 'sudo docker container run -d --rm --name jukibach-springboot -p 8081:8080 --network dev jukibach/springboot'
            }
        }

    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}