package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentListElementDto;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentOwnEditDto;

public class AppointmentMapper {

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private ImplantMapper implantMapper;

    /**
     * Metoda mapująca obiekt dto wizyty do edycji administratora na encję wizyty
     *
     * @param appointmentEditDto obiekt dto
     * @return encja wizyty
     */
    public Appointment createAppointmentFromEditDto(AppointmentEditDto appointmentEditDto) {
        Appointment appointment = new Appointment();
        appointment.setDescription(appointmentEditDto.getDescription());
        appointment.setStatus(appointmentEditDto.getStatus());
        return appointment;
    }

    public AppointmentEditDto createEditDtoFromAppointment(Appointment appointment) {
        AppointmentEditDto appointmentEditDto = new AppointmentEditDto();
        appointmentEditDto.setDescription(appointment.getDescription());
        appointmentEditDto.setId(appointment.getId());
        appointmentEditDto.setVersion(appointment.getVersion());
        appointmentEditDto.setStatus(appointment.getStatus());
        return appointmentEditDto;
    }

    /**
     * Metoda mapująca dane z encji Appointment na obiekt typu AppointmentListElementDto
     *
     * @param appointment obiekt encji Appointment
     * @return obiekt Dto
     */
    public AppointmentListElementDto appointmentListElementDtoFromAppointment(Appointment appointment) {
        AppointmentListElementDto appointmentListElementDto = new AppointmentListElementDto();
        appointmentListElementDto.setId(appointment.getId());
        appointmentListElementDto.setClient(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getClient())
        );
        appointmentListElementDto.setSpecialist(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getSpecialist())
        );
        appointmentListElementDto.setImplant(
                implantMapper.createImplantDtoFromImplant(appointment.getImplant())
        );
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

    public AppointmentDto createAppointmentDtoFromAppointment(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setVersion(appointment.getVersion());
        appointmentDto.setDescription(appointment.getDescription());
        appointmentDto.setStatus(appointment.getStatus());
        appointmentDto.setStartDate(appointment.getStartDate());
        appointmentDto.setEndDate(appointment.getEndDate());
        appointmentDto.setPrice(appointment.getPrice());
        appointmentDto.setClient(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getClient())
        );
        appointmentDto.setSpecialist(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getSpecialist())
        );
        appointmentDto.setImplant(
                implantMapper.createImplantDtoFromImplant(appointment.getImplant())
        );
        return appointmentDto;
    }
    /**
     * Metoda mapująca dane z DTO z danymi do edycji wizyty na wizytę
     *
     * @param appointmentOwnEditDto DTO z danymi do edycji wizyty
     * @return wizyta ze zmianami
     */
    public Appointment createAppointmentFromAppointmentOwnEditDto(AppointmentOwnEditDto appointmentOwnEditDto){
        Appointment appointment = new Appointment();
        appointment.setDescription(appointmentOwnEditDto.getDescription());
        appointment.setStatus(appointmentOwnEditDto.getStatus());
        appointment.setStartDate(appointmentOwnEditDto.getStartDate());
        return appointment;
    };
}
