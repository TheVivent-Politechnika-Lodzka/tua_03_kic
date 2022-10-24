package pl.lodz.p.it.ssbd2022.ssbd03.common;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Connection;

//@DataSourceDefinition( // Tworzenie struktur przy wdrażaniu aplikacji
//        name = "java:jboss/jdbc/ssbd03admin",
//        className = "org.mariadb.jdbc.MariaDbDataSource",
//        user = "ssbd03admin",
//        password = Config.password,
//        serverName = Config.serverName,
//        portNumber = 5432,
//        databaseName = Config.databaseName,
//        initialPoolSize = 1,
//        minPoolSize = 0,
//        maxPoolSize = 1,
//        maxIdleTime = 10)
//
//@DataSourceDefinition( // Implementacja uwierzytelniania w aplikacji
//        name = "java:jboss/jdbc/ssbd03auth",
//        className = "org.mariadb.jdbc.MariaDbDataSource",
//        user = "ssbd03auth",
//        password = Config.password,
//        serverName = Config.serverName,
//        portNumber = 5432,
//        databaseName = Config.databaseName)
//
//@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
//        name = "java:jboss/jdbc/ssbd03mok",
//        className = "org.mariadb.jdbc.MariaDbDataSource",
//        user = "ssbd03mok",
//        password = Config.password,
//        serverName = Config.serverName,
//        portNumber = 5432,
//        databaseName = Config.databaseName,
//        transactional = true,
//        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)
//
//@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
//        name = "java:jboss/jdbc/ssbd03mop",
//        className = "org.mariadb.jdbc.MariaDbDataSource",
//        user = "ssbd03mop",
//        password = Config.password,
//        serverName = Config.serverName,
//        portNumber = 5432,
//        databaseName = Config.databaseName,
//        transactional = true,
//        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03admin")
    private EntityManager em;
}