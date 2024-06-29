package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteProductServiceImpl implements DeleteProductService {

    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Long execute(Long productId) {
        Product product = getProductByIdOrThrow(productId);
        product.delete();
        return productRepository.save(product).getId();
    }

    private Product getProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 서비스가 존재하지 않습니다."));
    }
}
