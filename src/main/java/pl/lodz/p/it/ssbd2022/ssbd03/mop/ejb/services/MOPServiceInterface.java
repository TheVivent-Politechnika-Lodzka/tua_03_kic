package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import pl.lodz.p.it.ssbd2022.ssbd03.common.ServiceLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;
import java.util.UUID;

public interface MOPServiceInterface extends ServiceLocalInterface {

    /**
     * Metoda tworzy nowy wszczep
     * @param implant - nowy wszczep
     * @return - nowy wszczep
     */
    default Implant createImplant(Implant implant) {
        throw new MethodNotImplementedException();
    }

    default void deleteImplant(Implant implant) {
        throw new MethodNotImplementedException();
    }

    default Implant updateImplant(Implant implant) {
        throw new MethodNotImplementedException();
    }

    default Implant getImplantByName(String name){
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca liste wszczepów
     * @param page numer strony
     * @param pageSize  ilość pozycji na stronie
     * @param phrase szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws MethodNotImplementedException metoda nie jest zaimplementowana
     */
    default PaginationData findImplants(int page, int pageSize, String phrase, boolean archived) {
        throw new MethodNotImplementedException();
    }

    default List<Account> findSpecialists(int page, int pageSize, String phrase) {
        throw new MethodNotImplementedException();
    }

    // własne wizyty
    default List<Appointment> findVisits(int page, int pageSize, String phrase, String login) {
        throw new MethodNotImplementedException();
    }

    // wszyskie wizyty
    default List<Appointment> findVisits(int page, int pageSize, String phrase) {
        throw new MethodNotImplementedException();
    }

    default Appointment createAppointment(Appointment appointment) {
        throw new MethodNotImplementedException();
    }

    default Appointment editAppointment(Appointment appointment) {
        throw new MethodNotImplementedException();
    }

    default Appointment cancelAppointment(Appointment appointment) {
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

    default ImplantReview createReview(ImplantReview review) {
        throw new MethodNotImplementedException();
    }

    default void deleteReview(ImplantReview review) {
        throw new MethodNotImplementedException();
    }
}
