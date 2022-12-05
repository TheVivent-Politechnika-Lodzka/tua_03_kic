package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z niepoprawnymi danymi uwierzytelniającymi
 */
@ApplicationException(rollback = true)
public class InvalidCredentialException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String INVALID_CREDENTIAL = "server.error.appBase.invalidCredential";

    public InvalidCredentialException() {
        super(INVALID_CREDENTIAL, Response.Status.UNAUTHORIZED);
    }

    public InvalidCredentialException(Throwable cause) {
        super(INVALID_CREDENTIAL, cause, Response.Status.UNAUTHORIZED);
    }
}
