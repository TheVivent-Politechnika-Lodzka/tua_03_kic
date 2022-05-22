package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Klasa służąca do dodawania do każdego żądania nagłówków, w celu ominięcia CORS
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CORSFilter implements ContainerResponseFilter {

    /**
     * Metoda służąca do dodawania nagłówków w żądaniach
     * @param containerRequestContext Kontekst żądań kontenera
     * @param containerResponseContext Kontekst odpowiedzi kontenera
     * @throws IOException, gdy wystąpi błąd w metodzie
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", Config.WEBSITE_URL);
        containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Access-Control-Allow-Headers, Content-Type, Authorization, Origin, Accept");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        containerResponseContext.getHeaders().add("Access-Control-Request-Headers", "Authorization, Content-Type");
        containerResponseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
        if (containerRequestContext.getMethod().equals("OPTIONS")) {
            containerResponseContext.setStatus(200);
        }
    }

}
