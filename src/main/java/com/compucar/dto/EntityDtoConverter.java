package com.compucar.dto;

import java.util.List;

public interface EntityDtoConverter<E, D> {
    D convertToDto(E entity);
    List<D> convertToDtos(List<E> entities);
    E convertToEntity(D dto);
    List<E> convertToEntities(List<D> dtos);
}
