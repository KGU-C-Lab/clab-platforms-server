package page.clab.api.domain.position.application.port.in;

import page.clab.api.domain.position.application.dto.request.PositionRequestDto;

public interface RegisterPositionUseCase {
    Long registerPosition(PositionRequestDto requestDto);
}
