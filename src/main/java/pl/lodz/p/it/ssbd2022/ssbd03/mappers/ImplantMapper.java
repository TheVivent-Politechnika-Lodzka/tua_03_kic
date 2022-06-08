package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;

public class ImplantMapper {
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
                implant.getDuration()
        );
        return implantDto;

    }
}
