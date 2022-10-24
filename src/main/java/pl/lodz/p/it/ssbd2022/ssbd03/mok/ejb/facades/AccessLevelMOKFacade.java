package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelMOKFacade extends AbstractFacade<AccessLevel> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mok")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AccessLevelMOKFacade() {
        super(AccessLevel.class);
    }

    /**
     * Weryfikuje podany eTag
     *
     * @param entity - poziom dostępu do usunięcia
     * @throws DatabaseException
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public void remove(AccessLevel entity) {
        super.remove(entity);
    }

    /**
     * Tworzy nowy poziom dostępu
     *
     * @param entity - nowy poziom dostępu
     * @throws AccessLevelExistsException - jeśli dany poziom dostępu dla danego użytkownika już istnieje
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Override
    public void create(AccessLevel entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintViolations().contains(AccessLevel.CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE)) {
                throw new AccessLevelExistsException();
            }

            throw new DatabaseException(e);
        }
    }

}
