package page.clab.api.domain.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.position.application.dto.mapper.PositionDtoMapper;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionMyResponseDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RetrieveMyPositionsByYearUseCase;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPositionsByYearRetrievalService implements RetrieveMyPositionsByYearUseCase {

    private final RetrievePositionPort retrievePositionPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    public PositionMyResponseDto retrieveMyPositionsByYear(String year) {
        MemberPositionInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberPositionInfo();
        List<Position> positions = retrievePositionPort.findAllByMemberIdAndYearOrderByPositionTypeAsc(
                currentMemberInfo.getMemberId(), year);
        if (positions.isEmpty()) {
            throw new NotFoundException("해당 멤버의 " + year + "년도 직책이 존재하지 않습니다.");
        }
        return PositionDtoMapper.toPositionMyResponseDto(positions, currentMemberInfo);
    }
}
