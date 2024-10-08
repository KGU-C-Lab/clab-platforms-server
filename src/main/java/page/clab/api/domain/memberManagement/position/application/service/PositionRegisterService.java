package page.clab.api.domain.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.position.application.dto.mapper.PositionDtoMapper;
import page.clab.api.domain.memberManagement.position.application.dto.request.PositionRequestDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RegisterPositionUseCase;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class PositionRegisterService implements RegisterPositionUseCase {

    private final RegisterPositionPort registerPositionPort;
    private final RetrievePositionPort retrievePositionPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    public Long registerPosition(PositionRequestDto requestDto) {
        externalRetrieveMemberUseCase.ensureMemberExists(requestDto.getMemberId());
        return retrievePositionPort.findByMemberIdAndYearAndPositionType(
                        requestDto.getMemberId(), requestDto.getYear(), requestDto.getPositionType())
                .map(Position::getId)
                .orElseGet(() -> {
                    Position position = PositionDtoMapper.toPosition(requestDto);
                    return registerPositionPort.save(position).getId();
                });
    }
}
