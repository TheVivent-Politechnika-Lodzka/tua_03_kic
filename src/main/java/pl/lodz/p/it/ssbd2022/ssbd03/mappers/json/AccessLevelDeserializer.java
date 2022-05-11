package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

import java.io.IOException;
import java.lang.reflect.Type;

public class AccessLevelDeserializer implements JsonbDeserializer<AccessLevelDto> {

    @Override
    public AccessLevelDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        JsonObject jsonObject = jsonParser.getObject();
        String level = jsonObject.getString("level");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonObject.toString(), getAccessLevelClass(level));
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot deserialize access level: " + level);
        }
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
