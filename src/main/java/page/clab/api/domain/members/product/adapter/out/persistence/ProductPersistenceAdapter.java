package page.clab.api.domain.members.product.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.product.application.port.out.RegisterProductPort;
import page.clab.api.domain.members.product.application.port.out.RetrieveProductPort;
import page.clab.api.domain.members.product.application.port.out.UpdateProductPort;
import page.clab.api.domain.members.product.domain.Product;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements
        RegisterProductPort,
        UpdateProductPort,
        RetrieveProductPort {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Product update(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public Product getById(Long productId) {
        return repository.findById(productId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Product] id: " + productId + "에 해당하는 상품이 존재하지 않습니다."));
    }

    @Override
    public Page<Product> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Product> findByConditions(String productName, Pageable pageable) {
        return repository.findByConditions(productName, pageable)
                .map(mapper::toDomain);
    }
}
