package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "level"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataAdministratorDto.class, name = DataAdministrator.LEVEL_NAME),
        @JsonSubTypes.Type(value = DataClientDto.class, name = DataClient.LEVEL_NAME),
        @JsonSubTypes.Type(value = DataSpecialistDto.class, name = DataSpecialist.LEVEL_NAME)
})
public abstract class AccessLevelDto {

    private String level;

}
