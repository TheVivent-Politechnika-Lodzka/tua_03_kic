package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z niepoprawnym tokenem
 */
@ApplicationException(rollback = true)
public class TokenInvalidException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "server.error.appBase.tokenInvalid";

    public TokenInvalidException() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }

}
