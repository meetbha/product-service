package com.product.mapper;

import com.product.dto.ProductDto;
import com.product.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ProductMapper {

    Product productDtoToEntity(ProductDto productDto);
}
