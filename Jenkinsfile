pipeline {
    agent {
        docker {
            image '192.168.26.116:8082/base/builder:1.0.3'
            args '-v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock -v /root/.ssh:/root/.ssh'
        }
    }
    environment {
        DOCKER_REGISTRY = '192.168.26.116:8082'
        DOCKER_REPOSITORY = 'topband_back'
        GITURL  = sh(returnStdout: true, script: 'git remote -v | grep fetch | awk \'{print $2}\'')
    }

    parameters {
        gitParameter(name: 'BRANCH', branchFilter: 'origin/(feature.*|test.*|release.*|hotfix.*)', defaultValue: 'master', type: 'PT_BRANCH', description: 'Git分支参数')

        booleanParam(name: 'SHOP_CONSUMER', defaultValue: true, description: '选择部署shop-consumer')
        string(name: 'SHOP_CONSUMER_HOST', defaultValue: '192.168.26.161', description: 'shop-consumer主机')
        string(name: 'SHOP_CONSUMER_PORT', defaultValue: '7375', description: 'shop-consumer端口')
        string(name: 'SHOP_CONSUMER_PARAM', defaultValue: '-Xmx256m -Xms256m', description: 'shop-consumer启动参数')
        string(name: 'SHOP_CONSUMER_VERSION', defaultValue: '7.0.0.100', description: 'shop-consumer镜像版本')
        string(name: 'SHOP_CONSUMER_LOGS', defaultValue: '/data/software/tsmart/dubbo-mail/hyj-shop-consumer', description: 'shop-consumer日志挂载目录')

        booleanParam(name: 'SHOP_PROVIDER', defaultValue: true, description: '选择部署shop-provider')
        string(name: 'SHOP_PROVIDER_HOST', defaultValue: '192.168.26.161', description: 'shop-provider主机')
        string(name: 'SHOP_PROVIDER_PORT', defaultValue: '9798', description: 'shop-provider端口')
        string(name: 'SHOP_PROVIDER_PARAM', defaultValue: '-Xmx256m -Xms256m', description: 'shop-provider启动参数')
        string(name: 'SHOP_PROVIDER_VERSION', defaultValue: '7.0.0.100', description: 'shop-provider镜像版本')
        string(name: 'SHOP_PROVIDER_LOGS', defaultValue: '/data/software/tsmart/dubbo-mail/hyj-shop-provider', description: 'shop-provider日志挂载目录')

        booleanParam(name: 'SHOP_EMAIL', defaultValue: true, description: '选择部署shop-email')
        string(name: 'SHOP_EMAIL_HOST', defaultValue: '192.168.26.161', description: 'shop-email主机')
        string(name: 'SHOP_EMAIL_PORT', defaultValue: '8482', description: 'shop-email端口')
        string(name: 'SHOP_EMAIL_PARAM', defaultValue: '-Xmx256m -Xms256m', description: 'shop-email启动参数')
        string(name: 'SHOP_EMAIL_VERSION', defaultValue: '7.0.0.100', description: 'shop-email镜像版本')
        string(name: 'SHOP_EMAIL_LOGS', defaultValue: '/data/software/tsmart/dubbo-mail/hyj-shop-email', description: 'shop-email日志挂载目录')

        string(name: 'APP_ACTIVE', defaultValue: 'dev', description: '环境')
        string(name: 'NACOS_ADDRESS', defaultValue: '192.168.26.119:8848,192.168.26.120:8848,192.168.26.121:8848', description: 'nacos地址')
        string(name: 'NACOS_NAMESPACE', defaultValue: 'ddbf5fa9-db88-42f6-9b17-5d126289482a', description: 'nacos命名空间')
        string(name: 'LOGSTASH_ADDRESS', defaultValue: '192.168.26.119:5044', description: 'logstash地址，ip:port')
    }

    stages {
        stage('build') {
            steps {
                sh 'echo dubbo-shop-hyj build stage ...'
                sh 'mvn -B -DskipTests clean package -P${APP_ACTIVE} -U -f dubbo-shop-hyj/pom.xml -am'
            }
            post {
                failure {
                    echo 'Some steps fail!'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo dubbo-shop-hyj deploy stage ...'
                sh 'mvn -B -DskipTests deploy -P${APP_ACTIVE} -f dubbo-shop-hyj/pom.xml -am'
            }
        }

        stage('Deliver') {
            when {
                not {
                    equals expected: 'online',
                            actual: "${params.APP_ACTIVE}"
                }
            }
            parallel {
                stage('Deliver:shop-consumer') {
                    when {
                        expression { return params.SHOP_CONSUMER }
                    }
                    steps {
                        sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="artifactId"]/text()\' dubbo-shop-hyj/shop-consumer/pom.xml): > SHOP_CONSUMER_TAG'
                        sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="version"]/text()\' dubbo-shop-hyj/shop-consumer/pom.xml) >> SHOP_CONSUMER_TAG'
                        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'password', usernameVariable: 'username')]) {
                            sh 'docker -H ${SHOP_CONSUMER_HOST} login -u ${username} -p ${password} ${DOCKER_REGISTRY}'
                        }
                        sh 'docker -H ${SHOP_CONSUMER_HOST} pull ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_CONSUMER_TAG`'
                        sh 'docker -H ${SHOP_CONSUMER_HOST} rm -f $(cut -d: -f1 SHOP_CONSUMER_TAG | tr / .) || true'
                        sh 'docker -H ${SHOP_CONSUMER_HOST} run -d \
                                --log-opt max-size=10m --log-opt max-file=3 \
                                --name $(cut -d: -f1 SHOP_CONSUMER_TAG | tr / .) \
                                -p ${SHOP_CONSUMER_PORT}:${SHOP_CONSUMER_PORT} \
                                -e NACOS_ADDRESS=${NACOS_ADDRESS} \
                                -e APP_ACTIVE=${APP_ACTIVE} \
                                -e NACOS_NAMESPACE=${NACOS_NAMESPACE} \
                                -e JAVA_OPTS="${SHOP_CONSUMER_PARAM}" \
                                -e LOGSTASH_ADDR=${LOGSTASH_ADDRESS} \
                                -v ${SHOP_CONSUMER_LOGS}/logs:/home/$(cut -d: -f1 SHOP_CONSUMER_TAG | tr / .)/logs \
                                --restart always \
                                ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_CONSUMER_TAG`'
                        script {
                            timeout(1) {
                                //这里设置超时时间1分钟，不会出现一直卡在检查状态
                                sh 'docker -H ${HOST} logs $(cut -d: -f1 ${SHOP_CONSUMER_TAG} | tr / .)'
                            }
                        }
                    }
                }

                stage('Deliver:shop-provider') {
                	when {
                		expression { return params.SHOP_PROVIDER }
                	}
                	steps {
                		sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="artifactId"]/text()\' dubbo-shop-hyj/shop-provider/pom.xml): > SHOP_PROVIDER_TAG'
                		sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="version"]/text()\' dubbo-shop-hyj/shop-provider/pom.xml) >> SHOP_PROVIDER_TAG'
                		withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'password', usernameVariable: 'username')]) {
                			sh 'docker -H ${SHOP_PROVIDER_HOST} login -u ${username} -p ${password} ${DOCKER_REGISTRY}'
                		}
                		sh 'docker -H ${SHOP_PROVIDER_HOST} pull ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_PROVIDER_TAG`'
                		sh 'docker -H ${SHOP_PROVIDER_HOST} rm -f $(cut -d: -f1 SHOP_PROVIDER_TAG | tr / .) || true'
                		sh 'docker -H ${SHOP_PROVIDER_HOST} run -d \
                				--log-opt max-size=10m --log-opt max-file=3 \
                				--name $(cut -d: -f1 SHOP_PROVIDER_TAG | tr / .) \
                				-p ${SHOP_PROVIDER_PORT}:${SHOP_PROVIDER_PORT} \
                				-e NACOS_ADDRESS=${NACOS_ADDRESS} \
                				-e APP_ACTIVE=${APP_ACTIVE} \
                				-e NACOS_NAMESPACE=${NACOS_NAMESPACE} \
                				-e JAVA_OPTS="${SHOP_PROVIDER_TAG_PARAM}" \
                				-e LOGSTASH_ADDR=${LOGSTASH_ADDRESS} \
                				-v ${SHOP_PROVIDER_LOGS}/logs:/home/$(cut -d: -f1 SHOP_PROVIDER_TAG | tr / .)/logs \
                				--restart always \
                				${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_PROVIDER_TAG`'
                		script {
                			timeout(1) {
                				//这里设置超时时间1分钟，不会出现一直卡在检查状态
                				sh 'docker -H ${HOST} logs $(cut -d: -f1 ${SHOP_PROVIDER_TAG} | tr / .)'
                			}
                		}
                	}
                }

                stage('Deliver:shop-email') {
                    when {
                        expression { return params.SHOP_EMAIL }
                    }
                    steps {
                        sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="artifactId"]/text()\' dubbo-shop-hyj/shop-email/pom.xml): > SHOP_EMAIL_TAG'
                        sh 'echo -n $(xmllint --xpath \'/*[local-name()="project"]/*[local-name()="version"]/text()\' dubbo-shop-hyj/shop-email/pom.xml) >> SHOP_EMAIL_TAG'
                        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'password', usernameVariable: 'username')]) {
                            sh 'docker -H ${SHOP_EMAIL_HOST} login -u ${username} -p ${password} ${DOCKER_REGISTRY}'
                        }
                        sh 'docker -H ${SHOP_EMAIL_HOST} pull ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_EMAIL_TAG`'
                        sh 'docker -H ${SHOP_EMAIL_HOST} rm -f $(cut -d: -f1 SHOP_EMAIL_TAG | tr / .) || true'
                        sh 'docker -H ${SHOP_EMAIL_HOST} run -d \
                                --log-opt max-size=10m --log-opt max-file=3 \
                                --name $(cut -d: -f1 SHOP_EMAIL_TAG | tr / .) \
                                -p ${SHOP_EMAIL_PORT}:${SHOP_EMAIL_PORT} \
                                -e NACOS_ADDRESS=${NACOS_ADDRESS} \
                                -e APP_ACTIVE=${APP_ACTIVE} \
                                -e NACOS_NAMESPACE=${NACOS_NAMESPACE} \
                                -e JAVA_OPTS="${SHOP_EMAIL_TAG_PARAM}" \
                                -e LOGSTASH_ADDR=${LOGSTASH_ADDRESS} \
                                -v ${SHOP_EMAIL_LOGS}/logs:/home/$(cut -d: -f1 SHOP_EMAIL_TAG | tr / .)/logs \
                                --restart always \
                                ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/`cat SHOP_EMAIL_TAG`'
                        script {
                            timeout(1) {
                                //这里设置超时时间1分钟，不会出现一直卡在检查状态
                                sh 'docker -H ${HOST} logs $(cut -d: -f1 ${SHOP_EMAIL_TAG} | tr / .)'
                            }
                        }
                    }
                }
            }
        }
    }
}