package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;

import java.util.ResourceBundle;

@RequestScoped
public class InternationalizationProvider {

    private ResourceBundle resourceBundle;

    @Resource
    SessionContext sessionContext;

    @Inject
    HttpServletRequest request;

    @Inject
    AccountFacade accountFacade;

    public String getMessage(String message) {
        String login = sessionContext.getCallerPrincipal().getName();
        if(login.equals("ANONYMOUS")) {
            System.out.println(request.getLocale());
            resourceBundle = ResourceBundle.getBundle("messages", request.getLocale());
            return resourceBundle.getString(message);
        }
        Account account = accountFacade.findByLogin(login);
        resourceBundle = ResourceBundle.getBundle("messages", account.getLanguage());
        return resourceBundle.getString(message);
    }


}
