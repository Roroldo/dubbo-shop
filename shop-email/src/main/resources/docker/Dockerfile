FROM registry.cn-shenzhen.aliyuncs.com/topband_back/jdk1.8-ubuntu18:v1
MAINTAINER hyj <huangyijun@topband.com.cn>
ENV JAR_NAME=@project.build.finalName@.jar
ENV JAR_HOME=/home/@artifactId@
WORKDIR ${JAR_HOME}
COPY ${JAR_NAME}  .
COPY docker-entrypoint.sh .
RUN chmod u+x docker-entrypoint.sh && sed -i 's/\r//g' docker-entrypoint.sh
ENTRYPOINT ["./docker-entrypoint.sh"]