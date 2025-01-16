package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.commons.exceptions.RplMessageInternalServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class GenericBuilder<I, O> {

    protected I dto;
    protected final O entity;

    GenericBuilder(O entity) {
        this.entity = entity;
    }

    GenericBuilder(I dto, Class<O> entityClass) {
        this.dto = dto;
        try {
            this.entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RplMessageInternalServiceException("Erro ao criar instância da entidade", e, this.getClass());
        }
    }

    public O build() {
        mapCommonFields();
        mapSpecificFields();
        return entity;
    }

    protected void mapCommonFields() {
        if (dto == null) {
            return;
        }

        Field[] dtoFields = dto.getClass().getDeclaredFields();
        Field[] entityFields = entity.getClass().getDeclaredFields();

        for (Field dtoField : dtoFields) {
            for (Field entityField : entityFields) {
                if (dtoField.getName().equals(entityField.getName())
                        && dtoField.getType().equals(entityField.getType())) {
                    dtoField.setAccessible(true);
                    entityField.setAccessible(true);
                    try {
                        entityField.set(entity, dtoField.get(dto));
                    } catch (IllegalAccessException e) {
                        throw new RplMessageInternalServiceException("Erro ao copiar campo: " + dtoField.getName(), e, this.getClass());
                    }
                }
            }
        }
    }

    /**
     * Implementação genérica para mapear campos de tipos diferentes.
     */
    protected void mapSpecificFields() {
        if (dto == null) {
            return;
        }

        Field[] dtoFields = dto.getClass().getDeclaredFields();
        Field[] entityFields = entity.getClass().getDeclaredFields();

        for (Field dtoField : dtoFields) {
            for (Field entityField : entityFields) {
                if (dtoField.getName().equals(entityField.getName())) {
                    dtoField.setAccessible(true);
                    entityField.setAccessible(true);
                    validationFields(dtoField, entityField);
                }
            }
        }
    }

    private void validationFields(Field dtoField, Field entityField) {
        try {
            Object dtoValue = dtoField.get(dto);
            if (dtoValue != null) {
                Object mappedValue;

                if (isCollection(dtoField.getType()) && isCollection(entityField.getType())) {
                    mappedValue = mapCollection((Collection<?>) dtoValue, entityField);
                } else if (!dtoField.getType().equals(entityField.getType())) {
                    mappedValue = convertValue(dtoValue, entityField.getType());
                } else {
                    mappedValue = dtoValue;
                }

                entityField.set(entity, mappedValue);
            }
        } catch (IllegalAccessException e) {
            throw new RplMessageInternalServiceException("Erro ao mapear campo específico: " + dtoField.getName(), e, this.getClass());
        }
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private Collection<?> mapCollection(Collection<?> sourceCollection, Field entityField) {
        try {
            // Determine the type of the collection (e.g., List, Set).
            Collection<Object> targetCollection = createEmptyCollection(entityField.getType());

            // Get the generic type of the target collection.
            Class<?> targetGenericType = (Class<?>) ((ParameterizedType) entityField.getGenericType()).getActualTypeArguments()[0];

            for (Object sourceElement : sourceCollection) {
                if (sourceElement != null) {
                    // Map individual elements using the convertValue method or custom logic.
                    Object targetElement = convertValue(sourceElement, targetGenericType);
                    targetCollection.add(targetElement);
                }
            }

            return targetCollection;
        } catch (Exception e) {
            throw new RplMessageInternalServiceException("Erro ao mapear coleção: " + entityField.getName(), e, this.getClass());
        }
    }

    private Collection<Object> createEmptyCollection(Class<?> collectionType) {
        if (List.class.isAssignableFrom(collectionType)) {
            return new ArrayList<>();
        }
        if (Set.class.isAssignableFrom(collectionType)) {
            return new HashSet<>();
        }
        throw new RplMessageInternalServiceException("Tipo de coleção não suportado: " + collectionType, this.getClass());
    }


    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        // Se o valor já é do tipo esperado, retorna diretamente.
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        try {
            // Conversão para enums
            if (targetType.isEnum() && value instanceof String) {
                return Enum.valueOf((Class<Enum>) targetType, (String) value);
            }

            // Conversão para String
            if (targetType.equals(String.class)) {
                return value.toString();
            }

            // Conversão para números
            if (Number.class.isAssignableFrom(targetType)) {
                return convertToNumber(value, targetType);
            }

            // Conversão para boolean
            if (targetType.equals(Boolean.class) || targetType.equals(boolean.class)) {
                return Boolean.parseBoolean(value.toString());
            }

            // Conversão para LocalDateTime
            if (targetType.equals(LocalDateTime.class)) {
                return convertToLocalDateTime(value);
            }

            // Conversão para LocalDate
            if (targetType.equals(LocalDate.class)) {
                return convertToLocalDate(value);
            }

            // Conversão para LocalTime
            if (targetType.equals(LocalTime.class)) {
                return convertToLocalTime(value);
            }
        } catch (Exception e) {
            throw new RplMessageInternalServiceException("Erro ao converter valor " + value + " para " + targetType, e, this.getClass());
        }

        throw new RplMessageInternalServiceException("Conversão não suportada de " + value.getClass() + " para " + targetType, this.getClass());
    }

    private Number convertToNumber(Object value, Class<?> targetType) {
        if (value instanceof Number) {
            Number number = (Number) value;
            if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
                return number.intValue();
            }
            if (targetType.equals(Long.class) || targetType.equals(long.class)) {
                return number.longValue();
            }
            if (targetType.equals(Double.class) || targetType.equals(double.class)) {
                return number.doubleValue();
            }
            if (targetType.equals(Float.class) || targetType.equals(float.class)) {
                return number.floatValue();
            }
            if (targetType.equals(Short.class) || targetType.equals(short.class)) {
                return number.shortValue();
            }
            if (targetType.equals(Byte.class) || targetType.equals(byte.class)) {
                return number.byteValue();
            }
        }
        return Double.valueOf(value.toString());
    }

    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        }
        if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        }
        throw new RplMessageInternalServiceException("Não foi possível converter " + value + " para LocalDateTime", this.getClass());
    }

    private LocalDate convertToLocalDate(Object value) {
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        if (value instanceof String) {
            return LocalDate.parse((String) value);
        }
        throw new RplMessageInternalServiceException("Não foi possível converter " + value + " para LocalDate", this.getClass());
    }

    private LocalTime convertToLocalTime(Object value) {
        if (value instanceof String) {
            return LocalTime.parse((String) value);
        }
        throw new RplMessageInternalServiceException("Não foi possível converter " + value + " para LocalTime", this.getClass());
    }

}
