package com.wangyu;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

public interface BasicMapper {

    ModelMapper MODEL_MAPPER = new ModelMapper();

    default <S, T> T convertToDto(S source, Class<T> targetClass) {
        return MODEL_MAPPER.map(source, targetClass);
    }

    default <S, T> List<T> convertToDtoList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> convertToDto(source, targetClass))
                .collect(Collectors.toList());
    }
}
