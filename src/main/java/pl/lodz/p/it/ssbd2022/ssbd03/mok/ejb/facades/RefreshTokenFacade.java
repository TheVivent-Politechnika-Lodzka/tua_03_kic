package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.RefreshToken;
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
public class RefreshTokenFacade extends AbstractFacade<RefreshToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public RefreshTokenFacade() {
        super(RefreshToken.class);
    }


    /**
     * metoda tworzy token do resetu hasła
     *
     * @param entity
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public void create(RefreshToken entity) {
        super.create(entity);
    }

    /**
     * metoda usuwa token z bazy danych
     *
     * @param refreshToken
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void unsafeRemove(RefreshToken refreshToken) {
        super.unsafeRemove(refreshToken);
    }

    /**
     * metoda zwraca refreshToken do odświeżenia accessToken
     *
     * @param login
     * @return refreshToken
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    //TODO jako, że nie jest stosowana to proponuję to usunąć
    public RefreshToken findToken(String login) {
        try {
            TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findByLogin", RefreshToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
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
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public RefreshToken findByToken(String token) {
        try {
            TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findByToken", RefreshToken.class);
            typedQuery.setParameter("token", token);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (PersistenceException e) {
            throw new DatabaseException(e);
        }
    }
}
