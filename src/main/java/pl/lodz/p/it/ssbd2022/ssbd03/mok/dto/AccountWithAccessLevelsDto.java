package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithAccessLevelsDto implements Taggable {

    private UUID id;
    private Long version;

    @Login
    private String login;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    private String email;

    private boolean isActive;

    private boolean isConfirmed;

    @Url
    private String url;

    @NotNull(message = "server.error.validation.constraints.notNull.language")
    @JsonbTypeSerializer(LocaleSerializerDeserializer.class)
    @JsonbTypeDeserializer(LocaleSerializerDeserializer.class)
    private Locale language;

    private String captcha;

    private String url;

    private List<AccessLevelDto> accessLevels = new ArrayList<>();



}
