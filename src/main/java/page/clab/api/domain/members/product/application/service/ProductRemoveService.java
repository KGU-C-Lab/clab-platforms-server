package page.clab.api.domain.members.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.product.application.port.in.RemoveProductUseCase;
import page.clab.api.domain.members.product.application.port.out.RetrieveProductPort;
import page.clab.api.domain.members.product.application.port.out.UpdateProductPort;
import page.clab.api.domain.members.product.domain.Product;

@Service
@RequiredArgsConstructor
public class ProductRemoveService implements RemoveProductUseCase {

    private final RetrieveProductPort retrieveProductPort;
    private final UpdateProductPort updateProductPort;

    @Transactional
    @Override
    public Long removeProduct(Long productId) {
        Product product = retrieveProductPort.getById(productId);
        product.delete();
        return updateProductPort.update(product).getId();
    }
}
