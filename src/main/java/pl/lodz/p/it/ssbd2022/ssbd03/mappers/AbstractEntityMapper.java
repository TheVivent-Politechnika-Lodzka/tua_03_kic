package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractDto;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Stateless
public class AbstractEntityMapper {

    @Inject
    private HashAlgorithm hashAlgorithm;

    /**
     * Metoda konwertująca obiekt encji na obiekt typu DTO
     *
     * @param dto Obiekt DTO, który zostanie zmieniony oraz (finalnie) zwrócony
     * @param entity Encja, na podstawie której utowrzony zostanie obiekt typu DTO
     * @return Obiekt typu DTO
     */
    public AbstractDto dtoFromEntity(AbstractDto dto, AbstractEntity entity) {
        String eTag = hashAlgorithm.generateETag(
                entity.getId(),
                entity.getVersion()
        );
        dto.setETag(eTag);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setLastModified(entity.getLastModified());
        return dto;
    }


}
