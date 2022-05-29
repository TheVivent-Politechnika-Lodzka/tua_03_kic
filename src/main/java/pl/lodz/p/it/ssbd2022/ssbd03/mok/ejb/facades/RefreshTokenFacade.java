package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.RefreshToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RefreshTokenFacade extends AbstractFacade<RefreshToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private HashAlgorithm hashAlgorithm;

    public RefreshTokenFacade() {
        super(RefreshToken.class);
    }


    /**
     * metoda tworzy token do resetu hasła
     *
     * @param entity
     */
    @Override
    @PermitAll
    public void create(RefreshToken entity) {
        super.create(entity);
    }

    /**
     * metoda usuwa token z bazy danych
     *
     * @param refreshToken
     */
    @Override
    @PermitAll
    public void unsafeRemove(RefreshToken refreshToken) {
        super.unsafeRemove(refreshToken);
    }

    /**
     * metoda zwraca refreshToken do odświeżenia accessToken
     *
     * @param login
     * @return refreshToken
     */
    @PermitAll
    public RefreshToken findToken(String login) {
        // TODO: Poprawić obsługę wyjątku nie znalezionego tokenu
        try {
            TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findByLogin", RefreshToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (PersistenceException e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * metoda zwraca refreshToken do odświeżenia accessToken
     *
     * @param token
     * @return resetPasswordToken
     */
    @PermitAll
    public RefreshToken findByToken(String token) {
        // TODO: Poprawić obsługę wyjątku nie znalezionego tokenu
        try {
            TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findByToken", RefreshToken.class);
            typedQuery.setParameter("token", token);
            return typedQuery.getSingleResult();
        } catch (PersistenceException e) {
            throw new DatabaseException(e);
        }

    }

    /**
     * metoda zwraca tokeny przed podaną datą
     *
     * @return
     */
    @PermitAll
    public List<RefreshToken> findExpiredTokens() {
        TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findExpired", RefreshToken.class);
        typedQuery.setParameter("now", Instant.now());
        return typedQuery.getResultList();
    }
}
