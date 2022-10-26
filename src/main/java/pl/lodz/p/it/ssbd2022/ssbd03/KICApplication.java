package pl.lodz.p.it.ssbd2022.ssbd03;

import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/api")
public class KICApplication extends Application {

    private static final long serialVersionUID = 1L;

//    @Override
//    public Set<Object> getSingletons() {
//        MOXyJsonProvider moxyJsonProvider = new MOXyJsonProvider();
//        moxyJsonProvider.setIncludeRoot(true);
//
//        HashSet<Object> singletons = new HashSet<>(1);
//        singletons.add(moxyJsonProvider);
//        return singletons;
//    }

}