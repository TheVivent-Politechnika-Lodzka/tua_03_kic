package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.mappers;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.InternationalizationProvider;

/**
 * Klasa mapująca wyrzucone wyjątki aplikacyjne na odpowiedź HTTP
 */
@Provider
public class AppBaseExceptionMapper implements ExceptionMapper<AppBaseException> {

    @Inject
    InternationalizationProvider provider;

    /**
     * Metoda zwracająca odpowiedź w przypadku wyrzuconego wyjątku aplikacyjnego
     * Zwraca status kodu HTTP, wiadomość wyjątku oraz przyczynę (o ile nie jest ona wartością pustą)
     *
     * @param e Wyrzucony wyjątek w aplikacji
     * @return Odpowiedź HTTP serwera
     */
    @Override
    public Response toResponse(AppBaseException e) {
        String cause = e.getCause() != null ? "\n\nCause: " + e.getCause() : "";
        StringBuilder message = new StringBuilder();
        message.append(e.getMessage()).append(cause);
        return Response.status(e.getResponse().getStatus()).entity(provider.getMessage(message.toString())).build();
    }
}
