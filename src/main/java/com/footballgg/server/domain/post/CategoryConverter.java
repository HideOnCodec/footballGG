package com.footballgg.server.domain.post;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category,String> {
    @Override
    public String convertToDatabaseColumn(Category category) {
        if (Objects.isNull(category)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return category.toString();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return Category.valueOf(dbData);
    }
}
