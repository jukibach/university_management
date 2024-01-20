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
                sh 'chmod 666 /var/run/docker.sock'
            }
        }

        stage('Packaging/Pushing image') {

            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t jukibach/springboot .'
                    sh 'docker push jukibach/springboot'
                }
            }
        }

        stage('Deploy PostgreSQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull postgres'
                sh 'docker network create dev || echo "this network exists"'
                sh 'docker container stop jukibach-postgres || echo "this container does not exist" '
                sh 'echo y | docker container prune '
                sh 'docker volume rm jukibach-postgres-data || echo "no volume"'

                sh "docker run --name jukibach-postgres --network dev -v jukibach-postgres-data:/var/lib/postgres -e POSTGRESQL_PASSWORD=${POSTGRESQL_ROOT_LOGIN_PSW} -e POSTGRES_DATABASE=university_management -d postgres "
                sh 'docker ps -a'
                sh "docker exec -i jukibach-postgres postgres --user=postgres --password=${POSTGRESQL_ROOT_LOGIN_PSW} < script"
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull jukibach/springboot'
                sh 'docker container stop jukibach-springboot || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '

                sh 'docker container run -d --rm --name jukibach-springboot -p 8081:8080 --network dev jukibach/springboot'
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