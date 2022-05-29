package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Instant;
import java.util.List;


@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RunAs(Roles.ADMINISTRATOR)
public class ResetPasswordFacade extends AbstractFacade<ResetPasswordToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Inject @Getter
    private HashAlgorithm hashAlgorithm;

    public ResetPasswordFacade() {
        super(ResetPasswordToken.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * metoda tworzy token do resetu hasła
     * @param entity
     */
    @Override
    @PermitAll
    public void create(ResetPasswordToken entity) {
        super.create(entity);
    }

    /**
     * metoda usuwa token z bazy danych
     * @param resetPasswordToken
     */
    @Override
    @PermitAll
    public void unsafeRemove(ResetPasswordToken resetPasswordToken) {
        super.unsafeRemove(resetPasswordToken);
    }

    /**
     * metoda zwraca token do resetu hasła podanego użytkownika
     * @param login
     * @return resetPasswordToken
     */
    @PermitAll
    public ResetPasswordToken findResetPasswordToken(String login) {
        // TODO: Poprawić obsługę wyjątku nie znalezionego tokenu
        try {
            TypedQuery<ResetPasswordToken> typedQuery = em.createNamedQuery("ResetPassword.findByLogin", ResetPasswordToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (PersistenceException e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * metoda zwraca tokeny przed podaną datą
     * @param date
     * @return
     */
    @PermitAll
    public List<ResetPasswordToken> findResetPasswordToken(Instant date) {
        TypedQuery<ResetPasswordToken> typedQuery = em.createNamedQuery("ResetPassword.findBeforeDate", ResetPasswordToken.class);
        typedQuery.setParameter("date", date);
        return typedQuery.getResultList();
    }
}
