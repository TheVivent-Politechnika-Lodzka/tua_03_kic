package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z niepoprawnie podanymi parametrami w żądaniu
 */
@ApplicationException(rollback = true)
public class InvalidParametersException extends AppBaseException {

    private static final String INVALID_PARAMETERS = "server.error.appBase.invalidParameters";

    public InvalidParametersException() {
        super(INVALID_PARAMETERS, Response.Status.BAD_REQUEST);
    }

    public InvalidParametersException(Throwable cause) {
        super(INVALID_PARAMETERS, cause, Response.Status.BAD_REQUEST);
    }
}
