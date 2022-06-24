package pl.lodz.p.it.ssbd2022.ssbd03.common;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;

public class Roles {

    private static final long serialVersionUID = 1L;

    public static final String ADMINISTRATOR = DataAdministrator.LEVEL_NAME;
    public static final String CLIENT = DataClient.LEVEL_NAME;
    public static final String SPECIALIST = DataSpecialist.LEVEL_NAME;

    public static final String AUTHENTICATED = "AUTHENTICATED";
    public static final String ANONYMOUS = "ANONYMOUS";


}
