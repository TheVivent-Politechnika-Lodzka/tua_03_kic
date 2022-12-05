package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

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
