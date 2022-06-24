package pl.lodz.p.it.ssbd2022.ssbd03.security;



import com.google.gson.annotations.SerializedName;
import jakarta.persistence.SecondaryTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Klasa reprezentująca reCAPTCHA
 */
@Getter
@Setter
public class ReCaptcha implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private float score;
    private String action;
    private String challengeTs;
    private String hostname;

    @SerializedName("error-codes")
    private ErrorCodeForCaptchaEnum[] errorCodes;


    /**
     * Funkcja sprawdzająca czy jest to błąd związany z klientem (odpowiedz)
     * @return true jeżeli bład jest związany z parametrem odpowiedzi recaptcha
     * @return false jeżeli błąd nie jest związany z parametrem odpowiedzi recaptcha
     */
    public boolean isClientError() {

        ErrorCodeForCaptchaEnum[] errorCodes = this.errorCodes;

        if (errorCodes == null) {
            return false;
        }

        for (ErrorCodeForCaptchaEnum errorCode : errorCodes) {
            switch (errorCode) {
                case MissingInputResponse:
                case InvalidInputResponse:
                    return true;
            }
        }

        return false;
    }

    /**
     * Funkcja odpowiadająca za wygenetreowanie do stringa zawartości odpowiedzi z recaptcha
     * @return string zawierający czytelną odpowiedź recaptcha
     */
    @Override
    public String toString() {
        return "ReCaptcha{" +
                "success=" + success + '\'' +
                ", score=" + score + '\'' +
                ", action='" + action + '\'' +
                ", challengeTs='" + challengeTs + '\'' +
                ", hostname='" + hostname + '\'' +
                ", errorCodes=" + Arrays.toString(errorCodes) +
                '}';
    }
}
