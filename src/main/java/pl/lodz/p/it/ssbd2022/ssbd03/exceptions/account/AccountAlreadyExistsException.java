package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AccountAlreadyExistsException extends AppBaseException {

    private static final String ACCOUNT_ALREADY_EXISTS = "Account already exists";

    public AccountAlreadyExistsException() {
        super(ACCOUNT_ALREADY_EXISTS, Response.Status.BAD_REQUEST);
    }
}
