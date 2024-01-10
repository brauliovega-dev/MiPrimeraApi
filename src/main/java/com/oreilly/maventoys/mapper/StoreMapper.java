package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.dto.StoreDTO;
import com.oreilly.maventoys.models.Store;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    StoreDTO storeToStoreDTO(Store store);
    Store storeDTOToStore(StoreDTO storeDTO);
}

//El StoreMapper facilita la conversión entre el
// modelo de dominio (Store) y el DTO (StoreDTO),
// utilizando MapStruct para automatizar y
// estandarizar este proceso.
