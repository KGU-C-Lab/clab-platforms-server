package page.clab.api.domain.members.product.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.product.domain.Product;

public interface RetrieveProductPort {

    Product getById(Long productId);

    Page<Product> findAllByIsDeletedTrue(Pageable pageable);

    Page<Product> findByConditions(String productName, Pageable pageable);
}
