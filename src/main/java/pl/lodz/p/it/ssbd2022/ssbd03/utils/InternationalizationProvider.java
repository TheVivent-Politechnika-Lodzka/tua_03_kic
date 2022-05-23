package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;

import java.util.ResourceBundle;

/**
 * Klasa odpowiadająca za internacjonalizację komunikatów od strony serwera
 */
@RequestScoped
public class InternationalizationProvider {

    private ResourceBundle resourceBundle;

    @Inject
    HttpServletRequest request;

    @Inject
    AccountFacade accountFacade;

    /**
     * Metoda tłumacząca komunikaty na wybrany język
     * Język pobierany jest z nagłówka żądania
     *
     * @param message Komunikat, który ma zostać przetłumaczony
     * @return przetłumaczony komunikat
     */
    public String getMessage(String message) {
        resourceBundle = ResourceBundle.getBundle("messages", request.getLocale());
        return resourceBundle.getString(message);
    }
}
