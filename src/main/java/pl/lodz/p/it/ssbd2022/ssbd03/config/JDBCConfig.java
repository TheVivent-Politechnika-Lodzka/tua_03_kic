package pl.lodz.p.it.ssbd2022.ssbd03.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Connection;

@DataSourceDefinition( // Tworzenie struktur przy wdrażaniu aplikacji
        name = "java:app/jdbc/ssbd03admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03admin",
        password = "cyberpunk2077",
        serverName = "127.0.0.1",
//        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd03",
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10)

@DataSourceDefinition( // Implementacja uwierzytelniania w aplikacji
        name = "java:app/jdbc/ssbd03auth",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03auth",
        password = "cyberpunk2077",
        serverName = "127.0.0.1",
//        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd03")

@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
        name = "java:app/jdbc/ssbd03mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mok",
        password = "cyberpunk2077",
        serverName = "127.0.0.1",
//        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd03",
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition( // Operacje realizowane przez moduł aplikacji
        name = "java:app/jdbc/ssbd03mop",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mop",
        password = "cyberpunk2077",
        serverName = "127.0.0.1",
//        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd03",
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {
    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;


}
