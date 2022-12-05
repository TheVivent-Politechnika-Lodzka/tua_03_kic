package pl.lodz.p.it.ssbd2022.ssbd03.security;

import java.util.UUID;

public interface Taggable {

    UUID getId();

    Long getVersion();

    default String generateMessage() {
        return String.format("%s.%d", getId().toString(), getVersion());
    }
}
