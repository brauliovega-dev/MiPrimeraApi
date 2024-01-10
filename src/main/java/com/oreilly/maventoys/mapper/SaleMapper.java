    package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.dto.SaleDTO;
import com.oreilly.maventoys.models.Sales;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

    @Mapper
    public interface SaleMapper {
        SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

        SaleDTO saleToSaleDTO(Sales sale);
        Sales saleDTOToSale(SaleDTO saleDTO);
    }

