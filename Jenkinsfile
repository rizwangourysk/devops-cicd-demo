pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        DOCKER_IMAGE = 'rizwangourysk/devops-cicd-demo'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java') {
            steps {
                sh 'java -version'
                sh 'javac -version'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}'
            }
        }

        stage('Azure Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'azure-jenkins-sp',
                        usernameVariable: 'AZURE_CLIENT_ID',
                        passwordVariable: 'AZURE_CLIENT_SECRET'
                    )
                ]) {
                    sh '''
                        az login \
                            --service-principal \
                            --username "$AZURE_CLIENT_ID" \
                            --password "$AZURE_CLIENT_SECRET" \
                            --tenant "f8881560-4bc8-45bf-aab7-61915f660abb"

                        az account show \
                            --query "{subscription:name, user:user.name}" \
                            -o table
                    '''
                }
            }
        }

        stage('Connect to AKS') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'azure-jenkins-sp',
                        usernameVariable: 'AZURE_CLIENT_ID',
                        passwordVariable: 'AZURE_CLIENT_SECRET'
                    )
                ]) {
                    sh '''
                        az login \
                            --service-principal \
                            --username "$AZURE_CLIENT_ID" \
                            --password "$AZURE_CLIENT_SECRET" \
                            --tenant "f8881560-4bc8-45bf-aab7-61915f660abb"

                        az aks get-credentials \
                            --resource-group rizwan \
                            --name demo-aks1 \
                            --overwrite-existing

                        kubectl get nodes
                    '''
                }
            }
        }
    }
}


