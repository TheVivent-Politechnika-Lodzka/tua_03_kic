package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;


import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class ResourceNotFoundException extends AppBaseException{

    private static final String MESSAGE = "server.error.appBase.resourceNotFound";

    /**
     * Tworzy wyjątek związany z brakiem zasobu.
     * Kod odpowiedzi HTTP 404.
     * Zawiera zlokalizowany komunikat o braku zasobu.
     */
    public ResourceNotFoundException(){
        super(MESSAGE, Response.Status.NOT_FOUND);
    }

}
