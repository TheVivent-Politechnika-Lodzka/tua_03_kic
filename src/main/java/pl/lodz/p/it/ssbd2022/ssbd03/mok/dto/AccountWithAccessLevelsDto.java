package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import javax.json.bind.annotation.JsonbTypeDeserializer;
import javax.json.bind.annotation.JsonbTypeSerializer;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
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

    private List<AccessLevelDto> accessLevels = new ArrayList<AccessLevelDto>();


}
