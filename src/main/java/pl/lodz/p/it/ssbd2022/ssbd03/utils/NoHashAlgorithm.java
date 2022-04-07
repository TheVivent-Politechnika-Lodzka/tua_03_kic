package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.security.enterprise.identitystore.PasswordHash;

import java.util.Map;

//Klasa która nie hashuje nic :)

public class NoHashAlgorithm implements PasswordHash {
    @Override
    public void initialize(Map<String, String> parameters) {
        PasswordHash.super.initialize(parameters);
    }

    @Override
    public String generate(char[] chars) {
        return new String(chars);
    }

    @Override
    public boolean verify(char[] chars, String s) {
        return s.equals(new String(chars));
    }
}
