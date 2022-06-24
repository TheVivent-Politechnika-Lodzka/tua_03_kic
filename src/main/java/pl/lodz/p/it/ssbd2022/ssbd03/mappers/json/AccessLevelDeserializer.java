package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

import java.lang.reflect.Type;

public class AccessLevelDeserializer implements JsonbDeserializer<AccessLevelDto> {

    private static final long serialVersionUID = 1L;


    private static final Jsonb jsonb = JsonbBuilder.create();

    @Override
    public AccessLevelDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        JsonObject jsonObject = jsonParser.getObject();
        String level = jsonObject.getString("level");
        return jsonb.fromJson(jsonObject.toString(), getAccessLevelClass(level));
    }

    private Class<? extends AccessLevelDto> getAccessLevelClass(String level) {
        return switch (level) {
            case DataAdministrator.LEVEL_NAME -> DataAdministratorDto.class;
            case DataClient.LEVEL_NAME -> DataClientDto.class;
            case DataSpecialist.LEVEL_NAME -> DataSpecialistDto.class;
            default -> throw new IllegalArgumentException("Unknown access level: " + level);
        };
    }

}
