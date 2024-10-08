package page.clab.api.domain.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.position.application.dto.mapper.PositionDtoMapper;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionResponseDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RetrievePositionsByConditionsUseCase;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class PositionsByConditionsRetrievalService implements RetrievePositionsByConditionsUseCase {

    private final RetrievePositionPort retrievePositionPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> retrievePositions(String year, PositionType positionType, Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberPositionInfo();
        Page<Position> positions = retrievePositionPort.findByConditions(year, positionType, pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionDtoMapper.toPositionResponseDto(position, currentMemberInfo)));
    }
}
