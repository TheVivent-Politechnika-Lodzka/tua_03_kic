package pl.lodz.p.it.ssbd2022.ssbd03.common;

public class Config {

    // Dla JDBCConfig
    public static final String password = "cyberpunk2077";
    public static final String databaseName = "ssbd03";
    public static final String serverName = "127.0.0.1";
//    public static final String serverName = "studdev.it.p.lodz.pl";

    // Do SPA
    public static final String WEBSITE_URL = "http://localhost:3000";
//    public static final String WEBSITE_URL = "https://kic.agency:8403/";

    // Dla EmailConfig
    public static final String MAIL_LOGIN = "szury@kic.agency";
    public static final String MAIL_PASSWORD = "Password123!";
    public static final String MAIL_SMTP_HOST = "ssl0.ovh.net";
    public static final String MAIL_SMTP_SSL_ENABLE = "true";
    public static final String MAIL_SMTP_PORT = "465";
    public static final String MAIL_SMTP_AUTH = "true";

    // JWT
    public static final int JWT_EXPIRATION_SECONDS = 60 * 60; // 1h
    public static final String JWT_SECRET = "zjZi6JWZ99IT0Trx49MNitLpwPjQc81BOUZytttWprg=";

    // Ogólne
    public static final int MAX_TX_RETRIES = 3;
    public static final int REGISTER_TOKEN_EXPIRATION_SECONDS = 60 * 30; // 30min
    public static final int RESET_PASSWORD_TOKEN_EXPIRATION_SECONDS = 60 * 15; // 15min

    // reCaptcha
    public static final String GOOGLE_SESCRET = "6Lf85hEgAAAAADQOoPdm05cZcDl8aDp46kGGOORl";

}
