package pl.lodz.p.it.ssbd2022.ssbd03.security;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

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
     *
     * @param taggable
     * @return wiadomość z podpisem
     */
    public String tag(Taggable taggable) {
        String msg = Base64.getEncoder().encodeToString(taggable.generateMessage().getBytes());
        return msg + "." + tag(msg);
    }

    /**
     * Metoda sprawdzająca, czy podpis jest poprawny.
     * Użyteczne tylko do weryfikacji nagłówka If-Match.
     *
     * @param tagWithMessage wiadomość z podpisem
     * @throws InAppOptimisticLockException jeśli podpis jest niepoprawny
     */
    public void verifyTag(String tagWithMessage) {
        String[] parts = tagWithMessage.split("\\.");
        if (parts.length != 2)
            throw InAppOptimisticLockException.signatureNotCorrect();
        String msg = parts[0];
        String tag = parts[1];
        if (!tag.equals(tag(msg)))
            throw InAppOptimisticLockException.signatureNotCorrect();
    }

    /**
     * Metoda sprawdzająca, czy podpis jest poprawny.
     *
     * @param taggable
     * @throws InAppOptimisticLockException jeśli podpis jest niepoprawny
     */
    public void verifyTag(Taggable taggable) {
        String tagFromDto = tag(taggable);
        String tagFromHeader = headers.getHeaderString("If-Match");
        if (!tagFromDto.equals(tagFromHeader)) // equals uwzględnia null
            throw InAppOptimisticLockException.signatureNotCorrect();
    }

    /**
     * Metoda sprawdza, czy nagłówek If-Match zawiera poprawnie podpisany tag.
     *
     * @throws InAppOptimisticLockException jeśli podpis jest niepoprawny
     */
    public void verifyTag() {
        String tagFromHeader = headers.getHeaderString("If-Match");
        if (tagFromHeader == null)
            throw InAppOptimisticLockException.signatureNotCorrect();
        verifyTag(tagFromHeader);
    }

    /**
     * Metoda odzyskująca id z tagu
     * @param message - część tagu z wiadomością
     * @return UUID z tagu
     */
    public UUID getUUIDFromTag(String message) {
        String decodedMessage = new String(Base64.getDecoder().decode(message));
        return UUID.fromString(decodedMessage.split("\\.")[0]);
    }

    /**
     * Metoda odzyskująca id z tagu z nagłówka
     * @return UUID z tagu
     */
    public UUID getUUIDFromTag() {
        String tagFromHeader = headers.getHeaderString("If-Match");
        return getUUIDFromTag(tagFromHeader.split("\\.")[0]);
    }

    public Taggable getTagFromHeader() {
        UUID id = getUUIDFromTag();
        Long version = getVersionFromTag();

        return new Taggable() {
            @Override
            public UUID getId() {
                return id;
            }

            @Override
            public Long getVersion() {
                return version;
            }
        };
    }

    /**
     * Metoda odzyskująca wersję z tagu
     * @param message - część tagu z wiadomością
     * @return Long wersji
     */
    public Long getVersionFromTag(String message) {
        String decodedMessage = new String(Base64.getDecoder().decode(message));
        return Long.parseLong(decodedMessage.split("\\.")[1]);
    }

    /**
     * Metoda odzyskująca wersję z tagu z nagłówka
     * @return Long wersji
     */
    public Long getVersionFromTag() {
        String tagFromHeader = headers.getHeaderString("If-Match");
        return getVersionFromTag(tagFromHeader.split("\\.")[0]);
    }
}
