package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import jakarta.ws.rs.core.Response;

/**
 * wyjątek rzucany w przypadku niepowodzenia wysyłania wiadomości
 */
public class EmailNotSendException extends AppBaseException {

    private static final String MESSAGE = "server.error.appBase.emailNotSend";

    /**
     * Konstruktor wyjątku. Zwraca komunikat i status HTTP 200
     */
    public EmailNotSendException() {
        super(MESSAGE, Response.Status.OK);
    }

}
