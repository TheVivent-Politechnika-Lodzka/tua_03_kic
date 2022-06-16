package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek rzucany, gdy klient usuwa recenzje wszczepu innym użytkownikom
 */
public class ClientRemovesOtherReviewsException extends AppBaseException {

    private final static String MESSAGE = "server.error.appBase.clientRemovesOtherReviews";

    public ClientRemovesOtherReviewsException() {
        super(MESSAGE, Response.Status.FORBIDDEN);
    }
}
