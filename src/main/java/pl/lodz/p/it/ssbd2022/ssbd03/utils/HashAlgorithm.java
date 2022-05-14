package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class HashAlgorithm implements PasswordHash {
    @Override
    public void initialize(Map<String, String> parameters) {
        PasswordHash.super.initialize(parameters);
    }

    @Override
    public String generate(char[] toHash) {
        return DigestUtils.sha512Hex(new String(toHash));
    }
    @Override
    public boolean verify(char[] toHash, String hash) {
        return hash.equals(DigestUtils.sha512Hex(new String(toHash)));
    }

    public String generateDtoTag(UUID entityId, Long entityVersion) {
        String tag = entityId.toString() + entityVersion.toString();
        return DigestUtils.sha256Hex(tag);
    }

}
