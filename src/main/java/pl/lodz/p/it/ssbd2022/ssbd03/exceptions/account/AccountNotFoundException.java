package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AccountNotFoundException extends AppBaseException {

    // Tutaj zostanie zaimplementowany ciąg znaków z pliku do internacjonalizacji
    private static final String ACCOUNT_NOT_FOUND_BY_LOGIN = "Account not found!";

    public AccountNotFoundException() {
        super(ACCOUNT_NOT_FOUND_BY_LOGIN, Response.Status.NOT_FOUND);
    }
}
