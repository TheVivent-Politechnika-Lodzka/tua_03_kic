package pl.lodz.p.it.ssbd2022.ssbd03.common;

public class Config {

    // Dla JDBCConfig
    public static final String password = "cyberpunk2077";
    public static final String databaseName = "ssbd03";
    public static final String serverName = "127.0.0.1";
//    public static final String serverName = "studdev.it.p.lodz.


    // Dla EmailConfig
    public static final String MAIL_LOGIN = "szury@kic.agency";
    public static final String MAIL_PASSWORD = "Password123!";
    public static final String MAIL_SMTP_HOST = "ssl0.ovh.net";
    public static final String MAIL_SMTP_SSL_ENABLE = "true";
    public static final String MAIL_SMTP_PORT = "465";
    public static final String MAIL_SMTP_AUTH = "true";

    // Ogólne
    public static final int MAX_TX_RETRIES = 3;
}
