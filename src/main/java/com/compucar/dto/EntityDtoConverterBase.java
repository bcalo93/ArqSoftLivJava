package com.compucar.dto;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityDtoConverterBase <E, D> implements EntityDtoConverter<E, D> {
    
    @Override
    public List<D> convertToDtos(List<E> entities) {
        return entities.stream()
                .map(entity -> convertToDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public List<E> convertToEntities(List<D> dtos) {
        return dtos.stream()
                .map(dto -> convertToEntity(dto))
                .collect(Collectors.toList());
    }

    public abstract D convertToDto(E entity);
    public abstract E convertToEntity(D dto);
}
