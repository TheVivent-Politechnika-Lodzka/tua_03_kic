package pl.lodz.p.it.ssbd2022.ssbd03.security;

import kong.unirest.Unirest;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ReCaptchaException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Klasa reprezentująca serwis udostępniający reCaptcha
 */
@RequestScoped
public class ReCaptchaService {

    private static final long serialVersionUID = 1L;

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
        String URL = "https://www.google.com/recaptcha/api/siteverify?secret=" + Config.GOOGLE_SESCRET
                + "&response=" + response
                + "&remoteip=" + IP;
        ReCaptcha reCaptcha = Unirest.get(URL).asObject(ReCaptcha.class).getBody();

        if (!reCaptcha.isSuccess()) {
            throw ReCaptchaException.invalidError();
        }
    }
}
