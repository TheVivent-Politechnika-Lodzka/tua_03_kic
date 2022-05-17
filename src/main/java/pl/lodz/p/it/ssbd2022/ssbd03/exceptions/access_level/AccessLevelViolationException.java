package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

@ApplicationException(rollback = true)
public class AccessLevelViolationException extends AppBaseException {

    private final static String CLIENT_CANT_BE_SPECIALIST = "server.error.client_cant_be_specialist";
    private final static String SPECIALIST_CANT_BE_CLIENT = "server.error.specialist_cant_be_client";

    private AccessLevelViolationException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    public static AccessLevelViolationException clientCantBeSpecialist() {
        return new AccessLevelViolationException(CLIENT_CANT_BE_SPECIALIST);
    }

    public static AccessLevelViolationException specialistCantBeClient() {
        return new AccessLevelViolationException(SPECIALIST_CANT_BE_CLIENT);
    }


}
