package pl.lodz.p.it.ssbd2022.ssbd03.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum zawierający kody błędów dla kontrolera Captcha
 */
public enum ErrorCodeForCaptchaEnum {
    MissingInputSecret,
    InvalidInputSecret,
    MissingInputResponse,
    InvalidInputResponse,
    BadRequest,
    TimeoutOrDuplicate;

    /**
     * Mapa zawierająca kody błędów dla kontrolera Captcha
     */
    private static final Map<String, ErrorCodeForCaptchaEnum> lookup = new HashMap<>();

    static {
        lookup.put("missing-input-secret", MissingInputSecret);
        lookup.put("invalid-input-secret", InvalidInputSecret);
        lookup.put("missing-input-response", MissingInputResponse);
        lookup.put("invalid-input-response", InvalidInputResponse);
        lookup.put("bad-request", BadRequest);
        lookup.put("timeout-or-duplicate", TimeoutOrDuplicate);
    }

    /**
     * Funkcja zwracająca kod błędu dla kontrolera Captcha
     * @param code kod błędu
     * @return zwraca obiekt typu ErrorCodeForCaptchaEnum
     */
    public static ErrorCodeForCaptchaEnum get(String code) {
        return lookup.get(code.toLowerCase());
    }

}
