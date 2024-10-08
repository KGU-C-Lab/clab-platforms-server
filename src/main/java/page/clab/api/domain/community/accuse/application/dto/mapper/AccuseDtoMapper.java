package page.clab.api.domain.community.accuse.application.dto.mapper;

import page.clab.api.domain.community.accuse.application.dto.request.AccuseRequestDto;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseResponseDto;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;

import java.util.List;

public class AccuseDtoMapper {

    public static Accuse toAccuse(AccuseRequestDto requestDto, String memberId, AccuseTarget target) {
        return Accuse.builder()
                .memberId(memberId)
                .target(target)
                .reason(requestDto.getReason())
                .isDeleted(false)
                .build();
    }

    public static AccuseTarget toAccuseTarget(AccuseRequestDto requestDto) {
        return AccuseTarget.builder()
                .targetType(requestDto.getTargetType())
                .targetReferenceId(requestDto.getTargetId())
                .accuseCount(1L)
                .accuseStatus(AccuseStatus.PENDING)
                .build();
    }

    public static AccuseMyResponseDto toAccuseMyResponseDto(Accuse accuse) {
        return AccuseMyResponseDto.builder()
                .targetType(accuse.getTarget().getTargetType())
                .targetId(accuse.getTarget().getTargetReferenceId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getTarget().getAccuseStatus())
                .createdAt(accuse.getTarget().getCreatedAt())
                .build();
    }

    public static AccuseResponseDto toAccuseResponseDto(Accuse accuse, List<MemberBasicInfoDto> members) {
        return AccuseResponseDto.builder()
                .members(members)
                .targetType(accuse.getTarget().getTargetType())
                .targetId(accuse.getTarget().getTargetReferenceId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getTarget().getAccuseStatus())
                .accuseCount(accuse.getTarget().getAccuseCount())
                .createdAt(accuse.getCreatedAt())
                .build();
    }
}
