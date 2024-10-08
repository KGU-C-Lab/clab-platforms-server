package page.clab.api.domain.members.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.product.application.dto.mapper.ProductDtoMapper;
import page.clab.api.domain.members.product.application.dto.response.ProductResponseDto;
import page.clab.api.domain.members.product.application.port.in.RetrieveDeletedProductsUseCase;
import page.clab.api.domain.members.product.application.port.out.RetrieveProductPort;
import page.clab.api.domain.members.product.domain.Product;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedProductsRetrievalService implements RetrieveDeletedProductsUseCase {

    private final RetrieveProductPort retrieveProductPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ProductResponseDto> retrieveDeletedProducts(Pageable pageable) {
        Page<Product> products = retrieveProductPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(products.map(ProductDtoMapper::toProductResponseDto));
    }
}
