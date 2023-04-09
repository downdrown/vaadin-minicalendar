FROM eclipse-temurin:17-jre-alpine

ENV JETTY_PORT=8080
ENV JETTY_VERSION=11.0.14

RUN apk add --no-cache bash curl tar && \
    curl https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/$JETTY_VERSION/jetty-distribution-$JETTY_VERSION.tar.gz -o jetty.tar.gz && \
    tar xvf jetty.tar.gz && \
    mv jetty-distribution-$JETTY_VERSION /jetty && \
    rm jetty.tar.gz

COPY target/minicalendar-showcase-*.war /jetty/webapps/ROOT.war

WORKDIR /jetty
ENTRYPOINT ["./bin/jetty.sh", "run", "--module=http", "--http-port=$JETTY_PORT"]
EXPOSE $JETTY_PORT
