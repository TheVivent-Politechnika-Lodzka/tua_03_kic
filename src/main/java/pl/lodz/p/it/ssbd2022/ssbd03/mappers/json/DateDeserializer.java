package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;


import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.JsonObject;

import java.lang.reflect.Type;
import java.time.Instant;

public class DateDeserializer implements JsonbDeserializer<Instant>, JsonbSerializer<Instant> {

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        JsonObject jsonObject = jsonParser.getObject();
        return Instant.parse(jsonObject.getString("$date"));
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        jsonGenerator.writeStartObject();
        jsonGenerator.write("$date", instant.toString());
        jsonGenerator.writeEnd();
    }

}
