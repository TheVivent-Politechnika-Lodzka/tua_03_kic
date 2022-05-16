package pl.lodz.p.it.ssbd2022.ssbd03.common;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Config {

    public static final int MAX_TX_RETRIES = Integer.parseInt(Objects.requireNonNull(Dotenv.load().get("MAX_TX_RETRIES")));
    public static final String MAIL_LOGIN = Objects.requireNonNull(Dotenv.load().get("MAIL_LOGIN"));
    public static final String MAIL_PASSWORD = Objects.requireNonNull(Dotenv.load().get("MAIL_PASSWORD"));
    public static final String MAIL_SMTP_HOST = Objects.requireNonNull(Dotenv.load().get("MAIL_SMTP_HOST"));
    public static final String MAIL_SMTP_SSL_ENABLE = Objects.requireNonNull(Dotenv.load().get("MAIL_SMTP_SSL_ENABLE"));
    public static final String MAIL_SMTP_PORT = Objects.requireNonNull(Dotenv.load().get("MAIL_SMTP_PORT"));
    public static final String MAIL_SMTP_AUTH = Objects.requireNonNull(Dotenv.load().get("MAIL_SMTP_AUTH"));
}
