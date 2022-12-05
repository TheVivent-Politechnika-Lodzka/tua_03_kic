package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.AccessLevelDeserializer;

import javax.json.bind.annotation.JsonbTypeDeserializer;

@Getter
@Setter
@AllArgsConstructor
@JsonbTypeDeserializer(AccessLevelDeserializer.class)
public abstract class AccessLevelDto {

    private static final long serialVersionUID = 1L;

    private final String level;

    public AccessLevelDto() {
        switch (this.getClass().getSimpleName()) {
            case "DataAdministratorDto":
                level = DataAdministrator.LEVEL_NAME;
                break;
            case "DataSpecialistDto":
                level = DataSpecialist.LEVEL_NAME;
                break;
            case "DataClientDto":
                level = DataClient.LEVEL_NAME;
                break;
            default:
                level = null;
        }
    }

}
