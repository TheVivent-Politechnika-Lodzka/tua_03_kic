package pl.lodz.p.it.ssbd2022.ssbd03.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

//@Provider
//public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {
//    final ObjectMapper defaultObjectMapper;
//
//    public JacksonObjectMapperProvider() {
//        defaultObjectMapper = createDefaultMapper();
//    }
//
//    @Override
//    public ObjectMapper getContext(Class<?> type) {return defaultObjectMapper;}
//
//    public static ObjectMapper createDefaultMapper() {
//        final ObjectMapper jackson = new ObjectMapper();
//        // any changes to the ObjectMapper is up to you. Do what you like.
//        // The ParameterNamesModule is optional,
//        // it enables you to have immutable POJOs in java8
////        jackson.registerModule(new ParameterNamesModule());
//        jackson.disable(SerializationFeature.INDENT_OUTPUT);
//        jackson.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
//        jackson.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        return jackson;
//    }
//}