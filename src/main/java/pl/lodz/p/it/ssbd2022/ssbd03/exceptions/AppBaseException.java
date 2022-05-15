package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Główny wyjątek aplikacyjny będący reprezentacją wyjatkiem generycznym, po którym
 * wszystkie transakcje są anulowane
 */
@ApplicationException(rollback = true)
public class AppBaseException extends WebApplicationException {

    protected AppBaseException(String message, Response.Status status) {
        super(message, status);
    }

    protected AppBaseException(Throwable cause, Response.Status status) {
        super(cause, status);
    }

    protected AppBaseException(String message, Throwable cause, Response.Status status)  {
        super(message, cause, status);
    }



}
