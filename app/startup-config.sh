#!/usr/bin/env bash

data-source add --name=ssbd03auth --jndi-name=java:jboss/jdbc/ssbd03auth --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=ssbd03 --password=cyberpunk2077
data-source add --name=ssbd03admin --jndi-name=java:jboss/jdbc/ssbd03admin --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=ssbd03 --password=cyberpunk2077
data-source add --name=ssbd03mok --jndi-name=java:jboss/jdbc/ssbd03mok --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=ssbd03 --password=cyberpunk2077
data-source add --name=ssbd03mop --jndi-name=java:jboss/jdbc/ssbd03mop --driver-name=mariadb-java-client-2.7.6.jar --connection-url=jdbc:mariadb://KIC_DB:3306/ssbd03 --user-name=ssbd03 --password=cyberpunk2077
