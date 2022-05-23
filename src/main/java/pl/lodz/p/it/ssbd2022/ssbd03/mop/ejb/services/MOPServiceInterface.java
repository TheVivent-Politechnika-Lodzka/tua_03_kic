package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import pl.lodz.p.it.ssbd2022.ssbd03.common.ServiceLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;

import java.util.List;

public interface MOPServiceInterface extends ServiceLocalInterface {

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

    default List<Implant> findImplants(int page, int pageSize, String phrase) {
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

    default Appointment finishAppointment(Appointment appointment) {
        throw new MethodNotImplementedException();
    }

    default ImplantReview createReview(ImplantReview review) {
        throw new MethodNotImplementedException();
    }

    default void deleteReview(ImplantReview review) {
        throw new MethodNotImplementedException();
    }

}
