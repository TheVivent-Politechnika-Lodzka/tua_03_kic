package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

// TODO: Dodanie Javadoc
@ApplicationException(rollback = true)
public class AccountAlreadyExistsException extends AppBaseException {

    // TODO: zmiana na kody do internationalizacji
    private static final String LOGIN_ALREADY_EXISTS = "Account already exists";
    private static final String EMAIL_ALREADY_EXISTS = "Account already exists";

    private AccountAlreadyExistsException(String message) {
            super(message, Response.Status.CONFLICT);
        }

        static public AccountAlreadyExistsException loginExists() {
            return new AccountAlreadyExistsException(LOGIN_ALREADY_EXISTS);
        }

        static public AccountAlreadyExistsException emailExists() {
            return new AccountAlreadyExistsException(EMAIL_ALREADY_EXISTS);
        }
    }