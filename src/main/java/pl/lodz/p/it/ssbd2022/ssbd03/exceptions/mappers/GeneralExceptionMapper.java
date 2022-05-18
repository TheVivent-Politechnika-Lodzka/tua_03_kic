package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * funkcja mapująca wyjątek na odpowiedź
     *
     * @param e - wyjątek
     * @return odpowiedź
     */
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
    }

}