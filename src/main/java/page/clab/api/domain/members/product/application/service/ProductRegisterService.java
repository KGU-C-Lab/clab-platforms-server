package page.clab.api.domain.members.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.product.application.dto.mapper.ProductDtoMapper;
import page.clab.api.domain.members.product.application.dto.request.ProductRequestDto;
import page.clab.api.domain.members.product.application.port.in.RegisterProductUseCase;
import page.clab.api.domain.members.product.application.port.out.RegisterProductPort;
import page.clab.api.domain.members.product.domain.Product;

@Service
@RequiredArgsConstructor
public class ProductRegisterService implements RegisterProductUseCase {

    private final RegisterProductPort registerProductPort;

    @Transactional
    @Override
    public Long registerProduct(ProductRequestDto requestDto) {
        Product product = ProductDtoMapper.toProduct(requestDto);
        return registerProductPort.save(product).getId();
    }
}
