package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentDto;

public class AppointmentMapper {

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private ImplantMapper implantMapper;

    public AppointmentDto createAppointmentDtoFromAppointment(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto(
                appointment.getId(),
                appointment.getVersion(),
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getClient()),
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(appointment.getSpecialist()),
                implantMapper.createImplantDtoFromImplant(appointment.getImplant()),
                appointment.getStartDate(),
                appointment.getEndDate(),
                appointment.getPrice(),
                appointment.getDescription(),
                appointment.getStatus()
        );
        return appointmentDto;

    }
}
