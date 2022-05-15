package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z niepoprawnymi danymi uwierzytelniającymi
 */
public class InvalidCredentialException extends AppBaseException {

    private static final String INVALID_CREDENTIAL = "server.error.appBase.invalidCredential";

    public InvalidCredentialException() {
        super(INVALID_CREDENTIAL, Response.Status.UNAUTHORIZED);
    }

    public InvalidCredentialException(Throwable cause) {
        super(INVALID_CREDENTIAL, cause, Response.Status.UNAUTHORIZED);
    }
}
