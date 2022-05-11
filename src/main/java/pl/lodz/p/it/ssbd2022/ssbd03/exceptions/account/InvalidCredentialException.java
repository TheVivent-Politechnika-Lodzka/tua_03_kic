package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class InvalidCredentialException extends AppBaseException {

    // Tutaj zostanie zaimplementowany ciąg znaków z pliku do internacjonalizacji
    private static final String INVALID_CREDENTIAL = "Invalid email or password";

    public InvalidCredentialException() {
        super(INVALID_CREDENTIAL, Response.Status.UNAUTHORIZED);
    }
}
