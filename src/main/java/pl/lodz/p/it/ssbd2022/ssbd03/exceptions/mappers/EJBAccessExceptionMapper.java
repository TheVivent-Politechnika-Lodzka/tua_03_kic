package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import javax.ejb.EJBAccessException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Klasa mapująca wyrzucone wyjątki typu EJBAccessException na odpowiedź HTTP
 */
@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {

    private static final long serialVersionUID = 1L;


    /**
     * Metoda zwracająca odpowiedź w przypadku wyrzuconego wyjątku typu EJBAccessException
     * Zwraca kod 403 oraz wiadomość wyjątku
     *
     * @param e Wyrzucony wyjątek w aplikacji
     * @return Odpowiedź HTTP serwera
     */
    @Override
    public Response toResponse(EJBAccessException e) {
        return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
    }
}
