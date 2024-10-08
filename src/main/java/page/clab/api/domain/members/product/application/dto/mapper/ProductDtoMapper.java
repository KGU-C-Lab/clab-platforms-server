package page.clab.api.domain.members.product.application.dto.mapper;

import page.clab.api.domain.members.product.application.dto.request.ProductRequestDto;
import page.clab.api.domain.members.product.application.dto.response.ProductResponseDto;
import page.clab.api.domain.members.product.domain.Product;

public class ProductDtoMapper {

    public static Product toProduct(ProductRequestDto requestDto) {
        return Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .isDeleted(false)
                .build();
    }

    public static ProductResponseDto toProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .url(product.getUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
