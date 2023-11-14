package com.repuestosgaston.configuration.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE)
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        
     // Configuración para mapear ProductEntity a ProductResponseDTO
//        TypeMap<ProductEnity, ProductResponseDTO> productEntityToResponseDTO = modelMapper.createTypeMap(ProductEntity.class, ProductResponseDTO.class)
//                .addMapping(src -> src.getId(), ProductResponseDTO::setId)
//                .addMapping(src -> src.getName(), ProductResponseDTO::setName)
//                .addMapping(src -> src.getDescription(), ProductResponseDTO::setDescription)
//                .addMapping(src -> src.getPrice(), ProductResponseDTO::setPrice)
//                .addMapping(src -> src.getStock(), ProductResponseDTO::setStock)
//                .addMapping(src -> src.getCategory(), ProductResponseDTO::setCategoryResponse);

        return modelMapper;
    }
	
}
