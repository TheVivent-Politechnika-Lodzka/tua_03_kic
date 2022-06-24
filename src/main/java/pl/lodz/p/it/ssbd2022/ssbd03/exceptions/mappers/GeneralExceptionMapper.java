package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Klasa mapująca wyrzucone generyczne wyjątki na odpowiedź HTTP
 */
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final long serialVersionUID = 1L;

    /**
     * Metoda zwracająca odpowiedź w przypadku wyrzuconego wyjątku
     * Zwraca kod 500 oraz wiadomość wyjątku
     *
     * @param e Wyrzucony wyjątek w aplikacji
     * @return Odpowiedź HTTP serwera
     */
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
    }

}