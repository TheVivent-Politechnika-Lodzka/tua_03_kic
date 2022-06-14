package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentListElementDto;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppointmentMapper {


    /**
     * Metoda mapująca dane z encji Appointment na obiekt typu AppointmentListElementDto
     *
     * @param appointment obiekt encji Appointment
     * @return obiekt Dto
     */
    public AppointmentListElementDto appointmentListElementDtoFromAppointment(Appointment appointment) {
        AppointmentListElementDto appointmentListElementDto = new AppointmentListElementDto();
        appointmentListElementDto.setId(appointment.getId());
        appointmentListElementDto.setClient(appointment.getClient());
        appointmentListElementDto.setSpecialist(appointment.getSpecialist());
        appointmentListElementDto.setImplant(appointment.getImplant());
        appointmentListElementDto.setStartDate(appointment.getStartDate());
        appointmentListElementDto.setDescription(appointment.getDescription());
        appointmentListElementDto.setStatus(appointment.getStatus());
        return appointmentListElementDto;
    }


    /**
     * Metoda mapująca dane z listy wizyt na listę obiektów Dto dotyczących wizyt
     *
     * @param appointments lista encji wizyt
     * @return lista obiektów Dto
     */
    public List<AppointmentListElementDto> appointmentListElementDtoList(Collection<Appointment> appointments) {
        return null == appointments ? null : appointments.stream()
                .filter(Objects::nonNull)
                .map(this::appointmentListElementDtoFromAppointment)
                .collect(Collectors.toList());
    }
}
