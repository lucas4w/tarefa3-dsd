# Dockerfile para o DevContainer do projeto Java com Maven e Python
# Base image
FROM mcr.microsoft.com/devcontainers/base:bookworm
# As features no devcontainer.json cuidarão da instalação do Java, Maven e Python.
# Instalação do Maven
USER root

ENV MAVEN_VERSION=3.9.8
ENV MAVEN_HOME=/opt/maven

RUN wget https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xzvf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt \
    && mv /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} \
    && rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV PATH="${MAVEN_HOME}/bin:${PATH}"

USER vscode