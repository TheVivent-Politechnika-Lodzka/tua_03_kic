package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Klasa mapująca wyrzucone wyjątki na odpowiedź HTTP
 */
@Provider
public class AppBaseExceptionMapper implements ExceptionMapper<AppBaseException> {

    /**
     *
     * @param e
     * @return
     */
    @Override
    public Response toResponse(AppBaseException e) {
        String cause = e.getCause() != null ? "\n\nCause: " + e.getCause() : "";
        StringBuilder message = new StringBuilder();
        message.append(e.getMessage()).append(cause);
        return Response.status(e.getResponse().getStatus()).entity(message.toString()).build();
    }
}
