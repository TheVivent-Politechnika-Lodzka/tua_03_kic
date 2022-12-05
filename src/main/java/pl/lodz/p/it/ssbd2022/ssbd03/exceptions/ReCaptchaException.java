package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błądy związane z reCaptcha
 */
@ApplicationException(rollback = true)
public class ReCaptchaException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String RECAPTCHA_FORMAT = "server.error.reCaptcha.format";
    private static final String RECAPTCHA_INVALID = "server.error.reCaptcha.invalid";

    /**
     * Metoda będąca konstruktorem odpowiadającym za tworzenie wyjątków dotyczących błędów
     * w ramach reCaptcha
     *
     * @param message  Wiadomość zawarta w wyjątku
     * @param response Status, jaki zwróci wyjątek
     */
    private ReCaptchaException(String message, Response.Status response) {
        super(message, response);
    }

    /**
     * Metoda zwajacąca wyjątek w przypadku niepoprawnego formatu otrzymanej reCaptchy
     *
     * @return Wyjątek typu ReCaptchaException
     */
    public static ReCaptchaException formatError() {
        return new ReCaptchaException(RECAPTCHA_FORMAT, Response.Status.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Metoda zwajacąca wyjątek w przypadku niezgodnej reCaptchy
     *
     * @return Wyjątek typu ReCaptchaException
     */
    public static ReCaptchaException invalidError() {
        return new ReCaptchaException(RECAPTCHA_INVALID, Response.Status.NOT_ACCEPTABLE);
    }
}
