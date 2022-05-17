package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd z serializacją lub deserializacją
 */
@ApplicationException(rollback = true)
public class SerializationDeserializationException extends AppBaseException {

    private static final String UNABLE_TO_SERIALIZE = "server.error.appBase.serialization";
    private static final String UNABLE_TO_DESERIALIZE = "server.error.appBase.deserialization";

    public static SerializationDeserializationException deserializationError(String message, Throwable cause) {
        return new SerializationDeserializationException(UNABLE_TO_DESERIALIZE + message, cause);
    }

    public static SerializationDeserializationException serializationError(String message, Throwable cause) {
        return new SerializationDeserializationException(UNABLE_TO_SERIALIZE + message, cause);
    }

    private SerializationDeserializationException(String message, Throwable cause) {
        super(message, cause, Response.Status.BAD_REQUEST);
    }
}
