package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;

import java.time.Duration;

public class ImplantMapper {


    /**
     * Mapuje dane z obiektu CreateImplantDto na obiekt Implant
     *
     * @param implantDto
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
     * Mapper mapujący dane z Implantu na ImplantDto
     *
     * @param implant - dane Implantu
     * @return implantDto - dto dotyczące implantu
     */
    public ImplantDto createImplantDtoFromImplant(Implant implant) {
        ImplantDto implantDto = new ImplantDto(
                implant.getId(),
                implant.getVersion(),
                implant.getName(),
                implant.getDescription(),
                implant.getManufacturer(),
                implant.getPrice(),
                implant.isArchived(),
                implant.getPopularity(),
                implant.getDuration(),
                implant.getImage()
        );
        return implantDto;
    }
}
