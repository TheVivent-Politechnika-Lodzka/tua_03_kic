FROM bitnami/wildfly:26.1.2-debian-11-r18

ADD ./target/ssbd03-v1.0.1.war /opt/wildfly/standalone/deployments/

#CMD ["/opt/bitnami/wildfly/bin/add-user.sh", "admin", "admin", "--silent"]
CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]