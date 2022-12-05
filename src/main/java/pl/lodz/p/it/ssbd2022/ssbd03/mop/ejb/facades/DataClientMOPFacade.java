package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DataClientMOPFacade extends AbstractFacade<DataClient> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mop")
    @Getter
    private EntityManager entityManager;

    @Getter
    private Tagger tagger;

    public DataClientMOPFacade() {
        super(DataClient.class);
    }

    /**
     * Metoda zwracająca login użytkownika o podanym identyfikatorze
     *
     * @param id Identyfikator użytkownika
     * @return Login użytkownika o podanym identyfikatorze
     * @throws AccountNotFoundException   jeśli nie znaleziono użytkownika o podanym identyfikatorze
     * @throws InvalidParametersException jeśli podano niepoprawny identyfikator
     * @throws DatabaseException          jeśli wystąpił błąd w bazie danych
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    public String getLoginFromId(UUID id) throws AccountNotFoundException {
        try {
            TypedQuery<String> typedQuery = entityManager.createNamedQuery("DataClient.getLoginFromId", String.class);
            typedQuery.setParameter("id", id.toString());
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw AccountNotFoundException.notFoundById();
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }

    /**
     * Metoda zwracająca obiekt DataClient o podanym loginie
     *
     * @param login Login użytkownika
     * @return Obiekt DataClient o podanym loginie
     * @throws AccountNotFoundException   jeśli nie znaleziono użytkownika o podanym loginie
     * @throws InvalidParametersException jeśli podany login jest niepoprawny
     * @throws DatabaseException          jeśli wystąpił błąd z bazą danych
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    public DataClient findByLogin(String login) {
        try {
            TypedQuery<DataClient> typedQuery = entityManager.createNamedQuery("DataClient.findByLogin", DataClient.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw AccountNotFoundException.notFoundByLogin();
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }
}


