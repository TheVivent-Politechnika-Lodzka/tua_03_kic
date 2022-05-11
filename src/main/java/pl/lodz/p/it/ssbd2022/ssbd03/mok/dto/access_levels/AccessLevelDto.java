package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.AccessLevelDeserializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonbTypeDeserializer(AccessLevelDeserializer.class)
public abstract class AccessLevelDto {

    private String level;

}
