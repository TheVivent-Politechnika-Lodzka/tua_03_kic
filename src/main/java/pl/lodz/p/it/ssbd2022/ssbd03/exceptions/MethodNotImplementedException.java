package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z niezaimplementowaniem metody z interfejsu do komponentu EJB
 */
@ApplicationException(rollback = true)
public class MethodNotImplementedException extends AppBaseException{

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "server.error.appBase.methodNotImplemented";

    public MethodNotImplementedException() {
        super(MESSAGE, Response.Status.NOT_IMPLEMENTED);
    }

}
