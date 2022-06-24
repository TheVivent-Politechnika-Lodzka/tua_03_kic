package pl.lodz.p.it.ssbd2022.ssbd03.validation;

/**
 * Klasa zawierająca wyrażenia regularne potrzebne do walidacji pól
 */
public class Patterns {

    private static final long serialVersionUID = 1L;

    public static final String PASSWORD = "^(?=.*[\\p{Lu}])(?=.*[\\p{Ll}])(?=.*\\d).+$";
    public static final String LOGIN = "^[a-zA-Z]+[a-zA-Z0-9]*$";
    public static final String FIRST_NAME = "^\\p{L}+$";
    public static final String LAST_NAME = "^\\p{L}+$";
    public static final String PESEL = "\\d+";
    public static final String PHONE_NUMBER = "\\d+";
    public static final String URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
}
