package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

@ApplicationException(rollback = true)
public class AccountNotFoundException extends AppBaseException {

    private static final String ACCOUNT_NOT_FOUND_BY_LOGIN = "Account not found!";

    public AccountNotFoundException() {
        super(ACCOUNT_NOT_FOUND_BY_LOGIN, Response.Status.NOT_FOUND);
    }
}
