package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateless
public class AccessLevelMapper {

    // from entity to dto

    public DataClientDto createDataClientDtoFromEntity(DataClient dataClient) {
        return new DataClientDto(dataClient.getPhoneNumber(), dataClient.getPesel());
    }

    public DataAdministratorDto createDataAdministratorDtoFromEntity(DataAdministrator dataAdministratort) {
        return new DataAdministratorDto(dataAdministratort.getPhoneNumber(), dataAdministratort.getContactEmail());
    }

    public DataSpecialistDto createDataSpecialistDtoFromEntity(DataSpecialist dataSpecialist) {
        return new DataSpecialistDto(dataSpecialist.getContactEmail(), dataSpecialist.getPhoneNumber());
    }

    // from dto to entity
    public DataClient createDataClientFromDto(DataClientDto dataClientDto) {
        DataClient dataClient = new DataClient();
        dataClient.setPhoneNumber(dataClientDto.getPhoneNumber());
        dataClient.setPesel(dataClientDto.getPesel());

        return dataClient;
    }

    public DataAdministrator createDataAdministratorFromDto(DataAdministratorDto dataAdministratorDto) {
        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setContactEmail(dataAdministratorDto.getContactEmail());
        dataAdministrator.setPhoneNumber(dataAdministratorDto.getPhoneNumber());
        return dataAdministrator;
    }

    public DataSpecialist createDataSpecialistFromDto(DataSpecialistDto dataSpecialistDto) {
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setContactEmail(dataSpecialistDto.getContactEmail());
        dataSpecialist.setPhoneNumber(dataSpecialistDto.getPhoneNumber());
        return dataSpecialist;
    }

    ///
    public AccessLevelDto createAccessLevelDtoFromEntity(AccessLevel accessLevel) {
        if (accessLevel instanceof DataClient)
            return createDataClientDtoFromEntity((DataClient) accessLevel);
        if (accessLevel instanceof DataAdministrator)
            return createDataAdministratorDtoFromEntity((DataAdministrator) accessLevel);
        if (accessLevel instanceof DataSpecialist)
            return createDataSpecialistDtoFromEntity((DataSpecialist) accessLevel);
        return null;
    }

    public List<AccessLevelDto> createListOfAccessLevelDTO(Collection<AccessLevel> accessLevels) {
        return null == accessLevels ? null : accessLevels.stream()
                .filter(Objects::nonNull)
                .map(this::createAccessLevelDtoFromEntity)
                .collect(Collectors.toList());
    }

    public AccessLevel createAccessLevelFromDto(AccessLevelDto accessLevelDto) {

        if (accessLevelDto instanceof DataAdministratorDto dataAdministratorDto) {
            return createDataAdministratorFromDto(dataAdministratorDto);
        }

        if (accessLevelDto instanceof DataClientDto dataClientDto) {
            return createDataClientFromDto(dataClientDto);
        }

        if (accessLevelDto instanceof DataSpecialistDto dataSpecialistDto) {
            return createDataSpecialistFromDto(dataSpecialistDto);
        }

        throw new IllegalArgumentException("Unknown access level type");
    }

}