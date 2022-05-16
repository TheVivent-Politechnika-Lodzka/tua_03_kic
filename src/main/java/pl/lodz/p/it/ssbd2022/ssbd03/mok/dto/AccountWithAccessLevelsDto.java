package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.common.TaggedDto;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithAccessLevelsDto extends TaggedDto {

    @NotNull
    private String login;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private boolean isActive;

    @NotNull
    private boolean isConfirmed;

    @NotNull
    @JsonbTypeSerializer(LocaleSerializerDeserializer.class)
    @JsonbTypeDeserializer(LocaleSerializerDeserializer.class)
    private Locale language;

    private List<AccessLevelDto> accessLevels = new ArrayList<>();

}
