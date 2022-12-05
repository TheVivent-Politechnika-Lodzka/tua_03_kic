package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z nieodnalezieniem wizyty w bazie danych.
 */

@ApplicationException(rollback = true)
public class AppointmentNotFoundException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String APPOINTMENT_NOT_FOUND = "server.error.appBase.appointmentNotFound";

    /**
     * konstruktor wyjątku
     */
    public AppointmentNotFoundException() {
        super(APPOINTMENT_NOT_FOUND, Response.Status.NOT_FOUND);
    }
}
