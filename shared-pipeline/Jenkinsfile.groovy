// devsecops/shared-pipeline.groovy

def runSharedPipeline(String dockerImageName, String credentialsId, String projectId, String gkeCluster, String gkeZone, String namespace, String gitUrl) {
    pipeline {
        agent any
        environment {
            DOCKER_IMAGE_NAME = dockerImageName
            CREDENTIALS_ID = credentialsId
            PROJECT_ID = projectId
            GKE_CLUSTER = gkeCluster
            GKE_ZONE = gkeZone
            NAMESPACE = namespace
        }
        stages {
            stage('Checkout') {
                steps {
                    git branch: 'main', credentialsId: 'github-jenkins', url: gitUrl
                }
            }
            stage('Build') {
                steps {
                    script {
                        sh "docker build -t ${DOCKER_IMAGE_NAME} ."
                    }
                }
            }
            stage('Tag and Push') {
                steps {
                    sh "docker tag ${DOCKER_IMAGE_NAME} gcr.io/${PROJECT_ID}/${DOCKER_IMAGE_NAME}"
                    withCredentials([file(credentialsId: CREDENTIALS_ID, variable: 'GCLOUD_KEY')]) {
                        sh 'gcloud auth activate-service-account --key-file="$GCLOUD_KEY"'
                        sh "gcloud config set project ${PROJECT_ID}"
                        sh 'gcloud auth configure-docker'
                        sh "docker push gcr.io/${PROJECT_ID}/${DOCKER_IMAGE_NAME}"
                    }
                }
            }
            stage('Deploy to Gke Cluster') {
                steps {
                    withCredentials([file(credentialsId: CREDENTIALS_ID, variable: 'GCLOUD_KEY')]) {
                        sh 'gcloud auth activate-service-account --key-file="$GCLOUD_KEY"'
                        sh "gcloud container clusters get-credentials ${GKE_CLUSTER} --zone ${GKE_ZONE}"
                        sh "kubectl apply -f deployment.yaml --namespace ${NAMESPACE}"
                    }
                }
            }
        }
    }
}
