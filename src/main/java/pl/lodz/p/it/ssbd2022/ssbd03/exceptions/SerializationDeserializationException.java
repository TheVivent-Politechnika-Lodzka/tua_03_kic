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

    /**
     * Metoda statyczna zwracająca wyjątek SerializationDeserializationException
     * w przypadku gdy wystąpił błąd z deserializacją obiektu
     * @param message Wiadomość o niepowodzeniu deserializacji obiektu
     * @param cause Przyczynę niepowodzenia deserializacji obiektu
     * @return wyjatek typu SerializationDeserializationException
     */
    public static SerializationDeserializationException deserializationError(String message, Throwable cause) {
        return new SerializationDeserializationException(UNABLE_TO_DESERIALIZE + message, cause);
    }

    /**
     * Metoda statyczna zwracająca wyjątek SerializationDeserializationException
     * w przypadku gdy wystąpił błąd z serializacją obiektu
     * @param message Wiadomość o niepowodzeniu serializacji obiektu
     * @param cause Przyczynę niepowodzenia serializacji obiektu
     * @return wyjatek typu SerializationDeserializationException
     */
    public static SerializationDeserializationException serializationError(String message, Throwable cause) {
        return new SerializationDeserializationException(UNABLE_TO_SERIALIZE + message, cause);
    }

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie SerializationDeserializationException
     * @param message Wiadomość zawarta w wyjątku
     */
    private SerializationDeserializationException(String message, Throwable cause) {
        super(message, cause, Response.Status.BAD_REQUEST);
    }
}
