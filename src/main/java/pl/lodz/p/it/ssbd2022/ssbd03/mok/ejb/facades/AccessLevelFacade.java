package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevel> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private HashAlgorithm hashAlgorithm;

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    /**
     * @param entity - poziom dostępu do usunięcia
     * @param eTag   - eTag poziomu dostępu
     * @throws DatabaseException
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public void remove(AccessLevel entity, String eTag) {
        super.remove(entity, eTag);
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
            if (e.getConstraintName().contains(AccessLevel.CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE)) {
                throw new AccessLevelExistsException();
            }

            throw new DatabaseException(e);
        }
    }

}
