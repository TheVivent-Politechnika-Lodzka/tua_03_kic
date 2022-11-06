FROM bitnami/wildfly:26.1.2-debian-11-r18

USER root

ARG username
ARG password

ADD ./target/ssbd03-v1.0.1.war /opt/wildfly/standalone/deployments/
#COPY ./app/startup-config.cli /opt/jboss/startup-scripts/startup-config.cli

RUN /opt/bitnami/wildfly/bin/add-user.sh $username $password --silent
#    /opt/bitnami/wildfly/bin/jboss-cli.sh --connect --controller=127.0.0.1:9990 --user=$username --password=$password  && \
#    data-source add --name=ssbd03auth --jndi-name=java:jboss/jdbc/ssbd03auth --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=$username --password=$password && \
#    data-source add --name=ssbd03admin --jndi-name=java:jboss/jdbc/ssbd03admin --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=$username --password=$password && \
#    data-source add --name=ssbd03mok --jndi-name=java:jboss/jdbc/ssbd03mok --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=$username --password=$password && \
#    data-source add --name=ssbd03mop --jndi-name=java:jboss/jdbc/ssbd03mop --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=$username --password=$password

CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

