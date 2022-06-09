package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ListImplantDto;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImplantMapper {


    /**
     * Mapuje dane z obiektu CreateImplantDto na obiekt Implant
     * @param implantDto Obiekt CreateImplantDto
     * @return Implant - obiekt wszczepu
     */
    public Implant createImplantFromDto(CreateImplantDto implantDto) {
        Implant implant = new Implant();
        implant.setName(implantDto.getName());
        implant.setDescription(implantDto.getDescription());
        implant.setManufacturer(implantDto.getManufacturer());
        implant.setPrice(implantDto.getPrice());
        implant.setArchived(false);
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMillis(implantDto.getDuration()));
        implant.setImage(implantDto.getUrl());
        return implant;
    }

    /**
     * Mapuje dane z obiektu Implant na obiekt ListImplantDto
     * @param implant Obiekt Implant
     * @return ListImplantDto - obiekt wszczepu
     */
    public ListImplantDto listImplantDtoFromImplant(Implant implant) {
        ListImplantDto listImplantDto = new ListImplantDto();
        listImplantDto.setName(implant.getName());
        listImplantDto.setDescription(implant.getDescription());
        listImplantDto.setPrice(implant.getPrice());
        listImplantDto.setUrl(implant.getImage());
        return listImplantDto;
    }

    /**
     * Mapuje dane z obiektu Implant na obiekt AccountWithAccessLevelsDto
     * @param implants Lista obiektów Implant
     * @return List<ListImplantDto> - Lista obiektów ListImplantDto
     */
    public List<ListImplantDto> getListFromListImplantDtoFromImplant(Collection<Implant> implants){
        return null == implants ? null : implants.stream()
                .filter(Objects::nonNull)
                .map(this::listImplantDtoFromImplant)
                .collect(Collectors.toList());
    }


}
