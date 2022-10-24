package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review;

import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek rzucany, gdy klient usuwa recenzje wszczepu innym użytkownikom
 */
public class ClientRemovesOtherReviewsException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private final static String MESSAGE = "server.error.appBase.clientRemovesOtherReviews";

    public ClientRemovesOtherReviewsException() {
        super(MESSAGE, Response.Status.FORBIDDEN);
    }
}
