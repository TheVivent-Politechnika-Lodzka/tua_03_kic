package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

// TODO: Dodanie Javadoc
@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {


    // TODO: Dodanie Javadoc
    @Override
    public Response toResponse(EJBAccessException e) {
        return Response.status(Response.Status.FORBIDDEN).entity(e.getLocalizedMessage()).build();
    }
}
