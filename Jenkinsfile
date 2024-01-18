pipeline {
    agent any
    
    stages {
        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }
        
        stage('Build PostgreSQL image') {
            steps {
                sh 'docker build -t my-postgres:latest -f Dockerfile.postgres .'
            }
        }
        
        stage('Build Spring Boot image') {
            steps {
                sh 'docker build -t my-springboot:latest -f Dockerfile.springboot .'
            }
        }
        
        stage('Deploy PostgreSQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker container stop my-postgres || echo "this container does not exist" '
                sh 'echo y | docker container prune '
                sh 'docker volume rm my-postgres-data || echo "no volume"'

                sh 'docker run --name my-postgres --rm -v my-postgres-data:/var/lib/postgresql/data -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123123 -e POSTGRES_DB=db_example -p 5432:5432 -d my-postgres:latest'
                sh 'sleep 20'
            }
        }
        
        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker container stop my-springboot || echo "this container does not exist" '
                sh 'echo y | docker container prune '

                sh 'docker run -d --name my-springboot -p 8080:8080 --network host -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_example -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=123123 my-springboot:latest'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}
