package page.clab.api.domain.community.accuse.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.accuse.application.dto.mapper.AccuseDtoMapper;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseResponseDto;
import page.clab.api.domain.community.accuse.application.port.in.RetrieveAccusationUseCase;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccusationRetrievalService implements RetrieveAccusationUseCase {

    private final RetrieveAccusePort retrieveAccusePort;
    private final RetrieveAccuseTargetPort retrieveAccuseByTargetPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccuseResponseDto> retrieveAccusations(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        Page<AccuseTarget> accuseTargets = retrieveAccuseByTargetPort.findByConditions(type, status, countOrder, pageable);
        List<AccuseResponseDto> responseDtos = convertTargetsToResponseDtos(accuseTargets);
        return new PagedResponseDto<>(responseDtos, pageable, responseDtos.size());
    }

    private List<AccuseResponseDto> convertTargetsToResponseDtos(Page<AccuseTarget> accuseTargets) {
        return accuseTargets.stream()
                .map(accuseTarget -> {
                    List<Accuse> accuses = retrieveAccusePort.findByTargetOrderByCreatedAtDesc(accuseTarget.getTargetType(), accuseTarget.getTargetReferenceId());
                    if (accuses.isEmpty()) {
                        return null;
                    }
                    List<MemberBasicInfoDto> members = accuses.stream()
                            .map(accuse -> externalRetrieveMemberUseCase.getMemberBasicInfoById(accuse.getMemberId()))
                            .toList();
                    return AccuseDtoMapper.toAccuseResponseDto(accuses.getFirst(), members);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
