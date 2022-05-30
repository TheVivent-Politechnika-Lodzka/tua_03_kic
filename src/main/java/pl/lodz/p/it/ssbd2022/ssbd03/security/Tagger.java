package pl.lodz.p.it.ssbd2022.ssbd03.security;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Stateless
public class Tagger {

    @Context
    private HttpHeaders headers;

    private Mac mac;

    /**
     * Metoda inicjalizująca klasę podpisującą.
     */
    @PostConstruct
    public void init() {
        try {
            mac = Mac.getInstance("HmacSHA512");
            String secret = Config.E_TAG_SECRET;
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
            mac.init(secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda zwracająca podpis.
     *
     * @param base64message wiadomość do podpisania
     * @return sam podpis
     */
    public String tag(String base64message) {
        return Base64.getEncoder().encodeToString(mac.doFinal(base64message.getBytes()));
    }

    /**
     * Metoda generująca podpis wraz z wiadomością.
     * Format: <podpis>.<wiadomość>
     * @param taggable
     * @return wiadomość z podpisem
     */
    public String tag(Taggable taggable) {
        String msg = Base64.getEncoder().encodeToString(taggable.generateMessage().getBytes());
        return msg + "." + tag(msg);
    }

    /**
     * Metoda sprawdzająca czy podpis jest poprawny.
     * Użyteczne tylko do weryfikacji nagłówka If-Match.
     *
     * @param tagWithMessage wiadomość z podpisem
     * @return true jeśli podpis jest poprawny
     */
    public boolean verifyTag(String tagWithMessage) {
        String[] parts = tagWithMessage.split("\\.");
        if (parts.length != 2) {
            return false;
        }
        String msg = parts[0];
        String tag = parts[1];
        return tag.equals(tag(msg));
    }

    /**
     * Metoda sprawdzająca czy podpis jest poprawny.
     * @param taggable
     * @return true jeśli podpis jest poprawny
     */
    public boolean verifyTag(Taggable taggable) {
        String tagFromDto = tag(taggable);
        String tagFromHeader = headers.getHeaderString("If-Match");
        return tagFromDto.equals(tagFromHeader);
    }

}
