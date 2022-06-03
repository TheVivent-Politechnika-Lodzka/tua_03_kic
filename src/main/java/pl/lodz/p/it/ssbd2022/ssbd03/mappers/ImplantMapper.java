package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;

public class ImplantMapper {


    /**
     * Mapuje dane z obiektu CreateImplantDto na obiekt Implant
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
        implant.setDuration(implantDto.getDuration());
        implant.setImage(implantDto.getUrl());
        return implant;
    }
}
