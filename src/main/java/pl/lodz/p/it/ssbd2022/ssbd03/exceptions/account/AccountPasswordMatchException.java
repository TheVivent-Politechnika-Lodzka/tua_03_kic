package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AccountPasswordMatchException extends AppBaseException {

    // Tutaj zostanie zaimplementowany ciąg znaków z pliku do internacjonalizacji
    private static final String ACCOUNT_PASSWORD_MATCH_EXCEPTION = "Passwords do not match!";

    public AccountPasswordMatchException() {
        super(ACCOUNT_PASSWORD_MATCH_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}
