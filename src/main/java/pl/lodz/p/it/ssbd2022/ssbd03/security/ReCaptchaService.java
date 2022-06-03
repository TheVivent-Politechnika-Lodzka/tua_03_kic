package pl.lodz.p.it.ssbd2022.ssbd03.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import kong.unirest.Unirest;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ReCaptchaException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;

/**
 * Klasa reprezentująca serwis udostępniający reCaptcha
 */
@RequestScoped
public class ReCaptchaService {

    @Inject
    private Config config;

    @Context
    private HttpServletRequest httpServletRequest;

    /**
     * Metoda sprawdzająca poprawność właśnie zrobionej reCaptchy
     *
     * @param response Odpowiedź z komponentu reCaptchy
     */
    public void checkCaptchaValidation(String response) {
        if (response != null && response.isEmpty()) {
            throw ReCaptchaException.formatError();
        }

        String IP = httpServletRequest.getRemoteAddr();
        String URL = "https://www.google.com/recaptcha/api/siteverify?secret=" + config.GOOGLE_SESCRET
                + "&response=" + response
                + "&remoteip=" + IP;
        ReCaptcha reCaptcha = Unirest.get(URL).asObject(ReCaptcha.class).getBody();

        if(!reCaptcha.isSuccess()) {
            throw ReCaptchaException.invalidError();
        }
    }
}
