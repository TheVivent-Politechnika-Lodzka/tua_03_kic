FROM bitnami/wildfly:26.1.2-debian-11-r18

USER root

ARG username
ARG password

COPY ./app/standalone.xml /opt/bitnami/wildfly/standalone/configuration/standalone.xml
ADD ./target/ssbd03-v1.0.1.war /opt/bitnami/wildfly/standalone/deployments/

RUN /opt/bitnami/wildfly/bin/add-user.sh $username $password --silent
CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]