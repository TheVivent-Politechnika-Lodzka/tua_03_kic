package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
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
        switch (level) {
            case DataAdministrator.LEVEL_NAME:
                return DataAdministratorDto.class;
            case DataClient.LEVEL_NAME:
                return DataClientDto.class;
            case DataSpecialist.LEVEL_NAME:
                return DataSpecialistDto.class;
            default:
                throw new IllegalArgumentException("Unknown access level: " + level);
        }

    }

}
