package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantListElementDto;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImplantMapper {

    /**
     * Metoda konwertująca encję Implantu na DTO
     *
     * @param implant Obiekt Implantu
     * @return ImplantDto
     */
    public ImplantDto createImplantDtoFromImplant(Implant implant) {
        return new ImplantDto(
                implant.getId(),
                implant.getVersion(),
                implant.getName(),
                implant.getDescription(),
                implant.getManufacturer(),
                implant.getPrice(),
                implant.isArchived(),
                implant.getPopularity(),
                implant.getDuration(),
                implant.getImage());
    }

    /**
     * Mapuje dane z obiektu CreateImplantDto na obiekt Implant
     *
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
     * Mapuje dane z obiektu Implant na obiekt ImplantListElementDto
     *
     * @param implant Obiekt Implant
     * @return ImplantListElement - obiekt wszczepu
     */
    public ImplantListElementDto implantListElementDtoFromImplant(Implant implant) {
        ImplantListElementDto implantListElementDto = new ImplantListElementDto();
        implantListElementDto.setId(implant.getId());
        implantListElementDto.setName(implant.getName());
        implantListElementDto.setDescription(implant.getDescription());
        implantListElementDto.setPrice(implant.getPrice());
        implantListElementDto.setUrl(implant.getImage());
        return implantListElementDto;
    }

    /**
     * Mapuje dane z listy Implantów na liste obiektów ImplantListElementDto
     *
     * @param implants Lista obiektów Implant
     * @return List<ImplantListElementDto> - Lista obiektów ImplantListElementDto
     */
    public List<ImplantListElementDto> getListFromImplantListElementDtoFromImplant(Collection<Implant> implants) {
        return null == implants ? null : implants.stream()
                .filter(Objects::nonNull)
                .map(this::implantListElementDtoFromImplant)
                .collect(Collectors.toList());
    }


}
