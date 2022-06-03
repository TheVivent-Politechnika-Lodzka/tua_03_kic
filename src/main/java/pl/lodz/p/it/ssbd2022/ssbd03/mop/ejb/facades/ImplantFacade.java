package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RunAs(Roles.ADMINISTRATOR)
public class ImplantFacade extends AbstractFacade<Implant> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public ImplantFacade() {
        super(Implant.class);
    }

    /**
     * Metoda dodająca implant do bazy danych
     * @param entity - implant
     */
    @PermitAll
    @Override
    public void create(Implant entity) {
        super.create(entity);
    }


    /**
     * Metoda wyszukująca konkretny implant w bazie danych
     *
     * @param uuid - uuid implantu
     * @return implant
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    @PermitAll
    public Implant findByUUID(UUID uuid) {
        try {
            TypedQuery<Implant> typedQuery = entityManager.createNamedQuery("Implant.findById", Implant.class);
            typedQuery.setParameter("id", uuid);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (IllegalArgumentException iae) {
            throw new InvalidParametersException(iae.getCause());
        } catch (PersistenceException pe) {
            throw new DatabaseException(pe.getCause());
        }
    }


}