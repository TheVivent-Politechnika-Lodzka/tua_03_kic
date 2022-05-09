package pl.lodz.p.it.ssbd2022.ssbd03.common;

import com.fasterxml.jackson.core.util.JacksonFeature;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

//@Provider
//@Produces("application/json")
//public class JsonFeature implements Feature {
//
//    @Override
//    public boolean configure(FeatureContext context) {
//
//        context.property("jersey.config.server.disableMoxyJson", true);
//        // this is in jersey-media-json-jackson
//        context.register(JacksonFeature.class);
//
//        // or from jackson-jaxrs-json-provider
//        context.register(JacksonJsonProvider.class);
//        // for JAXB annotation support
//        context.register(JacksonJaxbJsonProvider.class);
//
//        return true;
//    }
//}
