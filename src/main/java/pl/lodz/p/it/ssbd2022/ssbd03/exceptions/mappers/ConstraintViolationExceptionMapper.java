package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.InternationalizationProvider;

import java.util.Iterator;


/**
 * Klasa mapująca wyrzucone wyjątki typu ConstraintViolationException na odpowiedź HTTP
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    InternationalizationProvider provider;

    /**
     * Metoda zwracająca odpowiedź w przypadku wyrzuconego wyjątku typu ConstraintViolationException
     * Zwraca kod 400 oraz wiadomość wyjątku
     *
     * @param e Wyrzucony wyjątek w aplikacji
     * @return Odpowiedź HTTP serwera
     */
    @Override
    public Response toResponse(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            //TODO w przyszlosci dodac wlasna adnotacje email
            if (getFieldName(cv.getPropertyPath()).equals("email")) {
                message.append(getFieldName(cv.getPropertyPath()))
                        .append(" - ").append(cv.getMessage())
                        .append("\n");
                continue;
            }
            message.append(provider.getMessage(cv.getMessage()))
                    .append("\n");
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(message.toString()).build();
    }

    // TODO: Dodanie Javadoc
    private String getFieldName(Path path) {
        Iterator<Path.Node> nodes = path.iterator();
        String fieldName = null;
        while (nodes.hasNext()) {
            fieldName = nodes.next().toString();
        }
        return fieldName;
    }
}
