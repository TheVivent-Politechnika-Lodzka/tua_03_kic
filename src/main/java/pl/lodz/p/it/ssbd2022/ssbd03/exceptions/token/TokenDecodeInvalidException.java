package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany ze źle utworzonym z tokenem
 */
@ApplicationException(rollback = true)
public class TokenDecodeInvalidException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "server.error.appBase.tokenDecodeInvalid";

    public TokenDecodeInvalidException() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }
}
