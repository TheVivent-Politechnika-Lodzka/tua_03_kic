package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.AccessLevelDeserializer;

@Getter @Setter
@AllArgsConstructor
@JsonbTypeDeserializer(AccessLevelDeserializer.class)
public abstract class AccessLevelDto {

    private final String level;

    public AccessLevelDto() {
        level = switch (this.getClass().getSimpleName()) {
            case "DataAdministratorDto" -> DataAdministrator.LEVEL_NAME;
            case "DataSpecialistDto" -> DataSpecialist.LEVEL_NAME;
            case "DataClientDto" -> DataClient.LEVEL_NAME;
            default -> null;
        };
    }

}
