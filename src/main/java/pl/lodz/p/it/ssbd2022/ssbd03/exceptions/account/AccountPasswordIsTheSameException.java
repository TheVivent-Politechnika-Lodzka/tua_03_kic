package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AccountPasswordIsTheSameException extends AppBaseException {

    // Tutaj zostanie zaimplementowany ciąg znaków z pliku do internacjonalizacji
    private static final String ACCOUNT_PASSWORD_IS_THE_SAME_EXCEPTION = "The new password is the same as the old one!";

    public AccountPasswordIsTheSameException() {
        super(ACCOUNT_PASSWORD_IS_THE_SAME_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}
