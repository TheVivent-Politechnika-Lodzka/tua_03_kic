package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
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

    @Inject
    private AbstractEntityMapper abstractEntityMapper;


    /**
     * Metoda tworząca obiekt typu DataClientDto na podstawie encji klienta
     *
     * @param dataClient Klient, który zostanie skonwertowany na DTO
     * @return DTO podanego klienta
     */
    public DataClientDto createDataClientDtoFromEntity(DataClient dataClient) {
        DataClientDto dto = new DataClientDto(dataClient.getPhoneNumber(), dataClient.getPesel());
        return (DataClientDto) abstractEntityMapper.dtoFromEntity(dto, dataClient);
    }

    /**
     * Metoda tworząca obiekt typu DataAdministratortDto na podstawie encji administratora
     *
     * @param dataAdministratort Administrator, który zostanie skonwertowany na DTO
     * @return DTO podanego administratora
     */
    public DataAdministratorDto createDataAdministratorDtoFromEntity(DataAdministrator dataAdministratort) {
        DataAdministratorDto dto = new DataAdministratorDto(dataAdministratort.getContactEmail(), dataAdministratort.getPhoneNumber());
        return (DataAdministratorDto) abstractEntityMapper.dtoFromEntity(dto, dataAdministratort);
    }

    /**
     * Metoda tworząca obiekt typu DataSpecialistDto na podstawie encji specjalisty
     *
     * @param dataSpecialist Specjalista, który zostanie skonwertowany na DTO
     * @return DTO podanego specjalisty
     */
    public DataSpecialistDto createDataSpecialistDtoFromEntity(DataSpecialist dataSpecialist) {
        DataSpecialistDto dto = new DataSpecialistDto(dataSpecialist.getContactEmail(), dataSpecialist.getPhoneNumber());
        return (DataSpecialistDto) abstractEntityMapper.dtoFromEntity(dto, dataSpecialist);
    }

    /**
     * Metoda konwertująca obiekt typu dotyczący specjalisty na jego encję
     *
     * @param dataClientDto DTO posiadające dane klienta
     * @return Encja klienta
     */
    public DataClient createDataClientFromDto(DataClientDto dataClientDto) {
        DataClient dataClient = new DataClient();
        dataClient.setPhoneNumber(dataClientDto.getPhoneNumber());
        dataClient.setPesel(dataClientDto.getPesel());

        return dataClient;
    }

    /**
     * Metoda konwertująca obiekt typu dotyczący specjalisty na jego encję
     *
     * @param dataAdministratorDto DTO posiadające dane administratora
     * @return Encja administratora
     */
    public DataAdministrator createDataAdministratorFromDto(DataAdministratorDto dataAdministratorDto) {
        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setContactEmail(dataAdministratorDto.getContactEmail());
        dataAdministrator.setPhoneNumber(dataAdministratorDto.getPhoneNumber());
        return dataAdministrator;
    }

    /**
     * Metoda konwertująca obiekt typu dotyczący specjalisty na jego encję
     *
     * @param dataSpecialistDto DTO posiadające dane specjalisty
     * @return Encja specjalisty
     */
    public DataSpecialist createDataSpecialistFromDto(DataSpecialistDto dataSpecialistDto) {
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setContactEmail(dataSpecialistDto.getContactEmail());
        dataSpecialist.setPhoneNumber(dataSpecialistDto.getPhoneNumber());
        return dataSpecialist;
    }

    /**
     * Metoda tworząca obiekt typu AccessLevelDto na podstawie encji poziomu dostępu
     *
     * @param accessLevel Poziom dostępu, z którego tworzone będzie DTO
     * @return DTO podanego AccessLevel
     */
    public AccessLevelDto createAccessLevelDtoFromEntity(AccessLevel accessLevel) {
        if (accessLevel instanceof DataClient)
            return createDataClientDtoFromEntity((DataClient) accessLevel);
        if (accessLevel instanceof DataAdministrator)
            return createDataAdministratorDtoFromEntity((DataAdministrator) accessLevel);
        if (accessLevel instanceof DataSpecialist)
            return createDataSpecialistDtoFromEntity((DataSpecialist) accessLevel);
        return null;
    }

    /**
     * Metoda zwracająca listę obiektów typu, skonwertowanych z kolekcji poziomów dostępu
     *
     * @param accessLevels Kolejka poziomów dostępu
     * @return Lista obiektów typu
     */
    public List<AccessLevelDto> createListOfAccessLevelDTO(Collection<AccessLevel> accessLevels) {
        return null == accessLevels ? null : accessLevels.stream()
                .filter(Objects::nonNull)
                .map(this::createAccessLevelDtoFromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Metoda tworząca AccessLevel z obiektu typu
     *
     * @param accessLevelDto Obiekt typu, z którego tworzny będzie AccessLevel
     * @return Obiekt poziomu dostępu
     */
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
