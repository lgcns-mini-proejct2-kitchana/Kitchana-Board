pipeline {
    agent any

    tools {
        gradle 'gradle8.12.1'
        jdk 'jdk17'
    }

    environment {
        GIT_TARGET_BRANCH = 'main'
        GIT_REPOSITORY_URL = 'https://github.com/lgcns-mini-proejct2-kitchana/Kitchana-Board.git'
    }

    parameters {
        string(name: 'TAG', defaultValue: 'latest', description: 'Docker Image Tag')
    }

    stages {
        stage('Github checkout') {
            steps {
                script {
                    echo "Cloning Repository"
                    git branch: "${GIT_TARGET_BRANCH}", url: "${GIT_REPOSITORY_URL}"
                }
            }
        }

        stage('Gradle Build') {
            steps {
                script {
                    echo "Starting Gradle Build"
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Build Docker Image & Push to ECR') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'kitchana-docker',  // EC2에 대한 SSH 구성 이름
                        transfers: [
                            sshTransfer(
                                cleanRemote: false,
                                excludes: '',
                                execCommand: """
                                    docker build -t ${env.AWS_ECR_URI}/kitchana/board:$TAG -f ./inner/DockerfileBoard .

                                    docker push ${env.AWS_ECR_URI}/kitchana/board:$TAG
                                """,
                                execTimeout: 180000,
                                flatten: false,
                                makeEmptyDirs: false,
                                noDefaultExcludes: false,
                                patternSeparator: '[, ]+',
                                remoteDirectory: './inner',
                                remoteDirectorySDF: false,
                                removePrefix: 'build/libs',
                                sourceFiles: 'build/libs/board-0.0.1-SNAPSHOT.jar'  // 'article' 관련 JAR만 선택
                            )
                        ],
                        usePromotionTimestamp: false,
                        useWorkspaceInPromotion: false,
                        verbose: false
                    )
                ])
            }
        }
    }

    post {
        success {
            echo 'pipeline succeeded'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}