pipeline {
    agent any
 // tools { 
 //        maven 'maven' 
 //    }
    stages {
        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }
            stage('Install Docker') {
      steps {
        sh 'sudo apt-get update'
        sh 'sudo apt-get install docker-ce'
        sh 'sudo systemctl start docker'
      }
    }
        stage('Build PostgreSQL image') {
            steps {
                sh 'docker build -t university-postgres:latest -f Dockerfile.postgres .'
            }
        }
        
        stage('Build Spring Boot image') {
            steps {
                sh 'docker build -t university-springboot:latest -f Dockerfile.springboot .'
                sh 'docker push anhanja123/springboot'
            }
        }
        
        stage('Deploy PostgreSQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker container stop my-postgres || echo "this container does not exist" '
                sh 'echo y | docker container prune '
                sh 'docker volume rm university-postgres-data || echo "no volume"'

                sh 'docker run --name university-postgres --rm -v my-postgres-data:/var/lib/postgresql/data -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123123 -e POSTGRES_DB=universitymanagement -p 5432:5432 -d university-postgres:latest'
                sh 'sleep 20'
            }
        }
        
        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker container stop university-springboot || echo "this container does not exist" '
                sh 'echo y | docker container prune '

                sh 'docker run -d --name university-springboot -p 8080:8080 --network host -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/universitymanagement -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=123123 university-postgres:latest'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}
