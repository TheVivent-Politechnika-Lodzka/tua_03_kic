package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import pl.lodz.p.it.ssbd2022.ssbd03.common.ServiceLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MOPServiceInterface extends ServiceLocalInterface {

    /**
     * Metoda tworzy nowy wszczep
     *
     * @param implant - nowy wszczep
     * @return - nowy wszczep
     * @throws MethodNotImplementedException jeśli serwis nie jest zaimplementowany
     */
    default Implant createImplant(Implant implant) {
        throw new MethodNotImplementedException();
    }

    /**
     * Serwis powiązany z MOP 2 - archiwizacja wszczepu
     *
     * @param implant - uuid archiwizowanego wszczepu
     * @return obiekt Implant
     * @throws MethodNotImplementedException jeśli serwis nie jest zaimplementowany
     */
    default Implant archiveImplant(UUID implant) {
        throw new MethodNotImplementedException();
    }

    default void deleteImplant(Implant implant) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca liste wszczepów
     *
     * @param uuid  uuid implantu do edycji
     * @param implant dane do modyfikacji implantu
     * @return zmodyfikowany implant
     * @throws MethodNotImplementedException metoda nie jest zaimplementowana
     */
    default Implant editImplant(UUID uuid, Implant implant) {
        throw new MethodNotImplementedException();
    }

    default Implant getImplantByName(String name) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca liste wszczepów
     *
     * @param page     numer strony
     * @param pageSize ilość pozycji na stronie
     * @param phrase   szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws MethodNotImplementedException metoda nie jest zaimplementowana
     */
    default PaginationData findImplants(int page, int pageSize, String phrase, boolean archived) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca wybrany wszczep
     *
     * @param uuid indentyfikator wybranego wszczepu
     * @return wszczep
     * @throws MethodNotImplementedException metoda nie jest zaimplementowana
     */
    default public Implant findImplantByUuid(UUID uuid) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca liste specialistów - MOP.6
     *
     * @param page     - numer strony (int)
     * @param pageSize - ilość pozycji na stronie (int)
     * @param phrase   - szukana fraza (String)
     * @return - lista specialistów (PaginationData)
     * @throws MethodNotImplementedException - metoda nie jest zaimplementowana
     */
    default PaginationData findSpecialists(int page, int pageSize, String phrase) {
        throw new MethodNotImplementedException();
    }

    // własne wizyty
    default List<Appointment> findVisits(int page, int pageSize, String phrase, String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca listę wszystkich wizyt
     *
     * @param page     numer aktualnie przeglądanej strony
     * @param pageSize ilość rekordów na danej stronie
     * @param phrase   wyszukiwana fraza
     * @return Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws MethodNotImplementedException w przypadku niezaimplementowanej metody
     */
    default PaginationData findVisits(int page, int pageSize, String phrase) {
        throw new MethodNotImplementedException();
    }
    /**
     * Metoda zwracająca wizytę
     *
     * @param uuid     id wizyty
     * @return szczegóły wizyty
     * @throws MethodNotImplementedException w przypadku niezaimplementowanej metody
     */
    default Appointment findVisit(UUID uuid, String clientLogin){
        throw new MethodNotImplementedException();
    }
    /**
     * Metoda zwracająca listę wszystkich wizyt dla podanego loginu
     *
     * @param page numer aktualnie przeglądanej strony
     * @param pageSize ilość rekordów na danej stronie
     * @param login wyszukiwana fraza
     * @return Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws MethodNotImplementedException w przypadku niezaimplementowanej metody
     */
    default PaginationData findVisitsByLogin(int page, int pageSize, String login){
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda tworząca nową wizytę
     *
     * @param clientLogin       - login klienta
     * @param specialistId      - identyfikator specjalisty
     * @param implantId         - identyfikator wszczepu
     * @param startDate         - data rozpoczęcia wizyty
     * @return nowa wizyta
     * @throws MethodNotImplementedException w przypadku niezaimplementowanej metody
     */
    default Appointment createAppointment(String clientLogin, UUID specialistId, UUID implantId, Instant startDate) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.9 - Zarezerwuj wizytę, dostępność specjalisty
     * @param specialistId  - id specjalisty
     * @param month         - miesiąc
     * @return lista dostępności
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    default List<Instant> getSpecialistAvailabilityInMonth(UUID specialistId, Instant month, Duration duration) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda pozwalająca na edycję wizyty przez administratora
     *
     * @param id          UUID edytowanej wizyty
     * @param appointment parametry wizyty do edycji
     * @return Zedytowana wizyta
     * @throws MethodNotImplementedException w przypadku braku zaimplementowania metody
     */
    default Appointment editAppointmentByAdministrator(UUID id, Appointment appointment) {
        throw new MethodNotImplementedException();
    }

    default Appointment editAppointment(UUID id, Appointment appointment) {
        throw new MethodNotImplementedException();
    }
    default Appointment editOwnAppointment(UUID id, Appointment appointment,String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda pozwalająca na odwołanie dowolnej wizyty, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu administratora
     *
     * @param id identyfikator wizyty, która ma zostać odwołana
     * @return Wizyta, która została odwołana
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Appointment cancelAnyAppointment(UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda pozwalająca na odwołanie własnej wizyty, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu klienta/specjalisty
     *
     * @param id identyfikator wizyty, która ma zostać odwołana
     * @return Wizyta, która została odwołana
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Appointment cancelOwnAppointment(UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zapewniająca możliwość oznaczenia wizyty jako zakończonej
     * @param id identyfikator wizyty
     * @param login login specjalisty oznaczającego wizytę jako zakończoną
     * @return wizyta oznaczona jako zakończona
     * @throws MethodNotImplementedException gdy metoda nie jest zaimplementowana
     */
    default Appointment finishAppointment(UUID id, String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda dodająca recenzję dla danego wszczepu, po zakończeniu wizyty
     *
     * @param review Recenzja wszczepu, napisana przez klienta
     * @return Recenzja wszczepu
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default ImplantReview createReview(ImplantReview review) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda usuwająca recenzję dla danego wszczepu
     *
     * @param id Identyfikator recenzji wszczepu, która ma zostać usunięta
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default void deleteReview(UUID id, String login) {
        throw new MethodNotImplementedException();
    }


    /**
     * Metoda zwracająca listę wszystkich recenzji dla danego wszczepu
     * @param page Aktualny numer strony
     * @param pageSize Ilość recenzji na pojedynczej stronie
     * @param id Identyfikator wszczepu
     * @return Lista recenzji dla wszczepu
     * @throws MethodNotImplementedException w przypadku niezaimplementowanej metody
     *
     */
    default PaginationData getAllImplantReviews(int page, int pageSize, UUID id) {
        throw new MethodNotImplementedException();
    }
}
