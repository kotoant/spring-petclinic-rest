FROM eclipse-temurin:20-jdk

# cd /usr/local/app
WORKDIR /usr/local/app

# create /usr/local/app/dump directory
RUN mkdir dump

# copy jetty/target/*-exec.jar to /usr/local/app/jetty.jar
COPY jetty/target/*-exec.jar jetty.jar

# copy netty/target/*-exec.jar to /usr/local/app/netty.jar
COPY netty/target/*-exec.jar netty.jar

# copy tomcat/target/*-exec.jar to /usr/local/app/tomcat.jar
COPY tomcat/target/*-exec.jar tomcat.jar

# copy undertow/target/*-exec.jar to /usr/local/app/undertow.jar
COPY undertow/target/*-exec.jar undertow.jar

# copy run-java.sh to /usr/local/app/run-java.sh
COPY run-java.sh run-java.sh

# make run-java.sh executable
RUN chmod u+x run-java.sh

# run java
CMD ["./run-java.sh"]
