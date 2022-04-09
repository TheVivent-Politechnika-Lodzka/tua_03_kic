package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;

@ApplicationScoped
public class HashAlgorithm implements PasswordHash {
    @Override
    public void initialize(Map<String, String> parameters) {
        PasswordHash.super.initialize(parameters);
    }

    @Override
    public String generate(char[] chars) {
        return DigestUtils.sha512Hex(new String(chars));
    }
    @Override
    public boolean verify(char[] chars, String s) {
        return s.equals(DigestUtils.sha512Hex(new String(chars)));
    }
}
