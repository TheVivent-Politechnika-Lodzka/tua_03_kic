package pl.lodz.p.it.ssbd2022.ssbd03.mappers.json;

import jakarta.json.JsonObject;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.SerializationDeserializationException;

import java.lang.reflect.Type;
import java.util.Locale;

public class LocaleSerializerDeserializer implements JsonbSerializer<Locale>, JsonbDeserializer<Locale> {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class XD {
        private String language;
    }

    @Override
    public void serialize(Locale locale, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
       serializationContext.serialize(new XD(locale.getLanguage()), jsonGenerator);
    }

    @Override
    public Locale deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        JsonObject jsonObject = jsonParser.getObject();
        String language = jsonObject.getString("language");
        try {
            return new Locale(language);
        } catch (Exception e) {
            throw SerializationDeserializationException.deserializationError(language, e.getCause());
        }
    }
}
