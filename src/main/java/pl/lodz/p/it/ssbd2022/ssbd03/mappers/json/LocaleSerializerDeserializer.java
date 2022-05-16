package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;

import java.lang.reflect.Type;
import java.util.Locale;

public class LocaleSerializerDeserializer implements JsonbSerializer<Locale>, JsonbDeserializer<Locale> {

    @Override
    public void serialize(Locale locale, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        jsonGenerator.write(locale.getLanguage());
    }

    @Override
    public Locale deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        JsonObject jsonObject = jsonParser.getObject();
        String language = jsonObject.getString("language");
        try {
            return new Locale(language);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot deserialize locale object: " + language);
        }
    }
}
