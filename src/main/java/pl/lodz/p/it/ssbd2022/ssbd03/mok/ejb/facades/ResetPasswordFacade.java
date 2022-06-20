package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Instant;
import java.util.List;


@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RunAs(Roles.ADMINISTRATOR)
public class ResetPasswordFacade extends AbstractFacade<ResetPasswordToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public ResetPasswordFacade() {
        super(ResetPasswordToken.class);
    }

    /**
     * metoda tworzy token do resetu hasła
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void create(ResetPasswordToken entity) {
        super.create(entity);
    }

    /**
     * metoda usuwa token z bazy danych
     *
     * @param resetPasswordToken
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void unsafeRemove(ResetPasswordToken resetPasswordToken) {
        super.unsafeRemove(resetPasswordToken);
    }

    /**
     * metoda zwraca token do resetu hasła podanego użytkownika
     *
     * @param login
     * @return resetPasswordToken
     */
    @RolesAllowed(Roles.ANONYMOUS)
    public ResetPasswordToken findToken(String login) {
        try {
            TypedQuery<ResetPasswordToken> typedQuery = entityManager
                    .createNamedQuery("ResetPassword.findByLogin", ResetPasswordToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (PersistenceException e) {
            throw new DatabaseException(e);
        }

    }
}
