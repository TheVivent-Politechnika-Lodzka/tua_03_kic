package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Connection;

@DataSourceDefinition( // Tworzenie struktur przy wdrażaniu aplikacji
        name = "java:app/jdbc/ssbd03admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03admin",
        password = Config.password,
        serverName = Config.serverName,
        portNumber = 5432,
        databaseName = Config.databaseName,
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10)

@DataSourceDefinition( // Implementacja uwierzytelniania w aplikacji
        name = "java:app/jdbc/ssbd03auth",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03auth",
        password = Config.password,
        serverName = Config.serverName,
        portNumber = 5432,
        databaseName = Config.databaseName)

@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
        name = "java:app/jdbc/ssbd03mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mok",
        password = Config.password,
        serverName = Config.serverName,
        portNumber = 5432,
        databaseName = Config.databaseName,
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
        name = "java:app/jdbc/ssbd03mop",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mop",
        password = Config.password,
        serverName = Config.serverName,
        portNumber = 5432,
        databaseName = Config.databaseName,
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {
    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;
}
