package pl.lodz.p.it.ssbd2022.ssbd03;

import com.fasterxml.jackson.core.util.JacksonFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.base.JsonMappingExceptionMapper;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.base.JsonParseExceptionMapper;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ApplicationPath("/api")
public class KICApplication extends Application {
//    @Override
//    public Set<Object> getSingletons() {
//        Set<Object> set = new HashSet<>();
//        set.add(new JacksonJsonProvider().configure(SerializationFeature.INDENT_OUTPUT, true));
//        return set;
//    }
//
//    @Override
//    public Map<String, Object> getProperties() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("jersey.config.disableMoxyJson.server", true);
//        return map;
//    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ObjectMapper.class);
//        classes.add(JacksonFeature.class);
//        classes.add(JacksonObjectMapperProvider.class);
        return classes;
    }
}