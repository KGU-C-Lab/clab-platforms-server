package page.clab.api.domain.members.product.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.product.domain.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductJpaEntity toEntity(Product product);

    Product toDomain(ProductJpaEntity jpaEntity);
}
