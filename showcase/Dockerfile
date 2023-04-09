FROM eclipse-temurin:17-jre-alpine

ENV HTTP_PORT=8080
ENV TOMCAT_MAJOR=10
ENV TOMCAT_VERSION=10.1.7
ENV TOMCAT_TGZ_URL=https://dlcdn.apache.org/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
ENV CATALINA_HOME=/opt/tomcat
ENV CATALINA_OPTS="-Dhttp.port=${HTTP_PORT}"
ENV PATH $CATALINA_HOME/bin:$PATH

RUN echo "Installing required additional packages" && \
    apk add --no-cache bash curl tar

RUN echo "Prepare environment" && \
    mkdir --parents $CATALINA_HOME

RUN echo "Download Tomcat ${TOMCAT_VERSION}" && \
    curl ${TOMCAT_TGZ_URL} -o tomcat.tar.gz

RUN echo "Extracting Tomcat" && \
    tar xf tomcat.tar.gz --strip 1 --directory $CATALINA_HOME && \
    rm tomcat.tar.gz

RUN echo "Set catalina.sh permissions" && \
    chmod +x ${CATALINA_HOME}/bin/catalina.sh

RUN echo "Remove Tomcat default app" && \
    rm -rf $CATALINA_HOME/webapps/ROOT

COPY target/minicalendar-showcase-*.war $CATALINA_HOME/webapps/ROOT.war

WORKDIR /
ENTRYPOINT ["catalina.sh", "run"]
EXPOSE $HTTP_PORT
