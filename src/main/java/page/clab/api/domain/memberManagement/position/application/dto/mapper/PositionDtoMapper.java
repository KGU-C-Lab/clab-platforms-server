package page.clab.api.domain.memberManagement.position.application.dto.mapper;

import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.position.application.dto.request.PositionRequestDto;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionMyResponseDto;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionResponseDto;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PositionDtoMapper {

    public static Position toPosition(PositionRequestDto positionRequestDto) {
        return Position.builder()
                .memberId(positionRequestDto.getMemberId())
                .positionType(positionRequestDto.getPositionType())
                .year(positionRequestDto.getYear())
                .isDeleted(false)
                .build();
    }

    public static PositionMyResponseDto toPositionMyResponseDto(List<Position> positions, MemberPositionInfoDto memberInfo) {
        Map<String, List<PositionType>> positionTypesByYear = positions.stream()
                .collect(Collectors.groupingBy(
                        Position::getYear,
                        Collectors.mapping(Position::getPositionType, Collectors.toList())
                ));
        return PositionMyResponseDto.builder()
                .name(memberInfo.getMemberName())
                .email(memberInfo.getEmail())
                .imageUrl(memberInfo.getImageUrl())
                .interests(memberInfo.getInterests())
                .githubUrl(memberInfo.getGithubUrl())
                .positionTypes(positionTypesByYear)
                .build();
    }

    public static PositionResponseDto toPositionResponseDto(Position position, MemberPositionInfoDto memberInfo) {
        return PositionResponseDto.builder()
                .id(position.getId())
                .name(memberInfo.getMemberName())
                .email(memberInfo.getEmail())
                .imageUrl(memberInfo.getImageUrl())
                .interests(memberInfo.getInterests())
                .githubUrl(memberInfo.getGithubUrl())
                .positionType(position.getPositionType())
                .year(position.getYear())
                .build();
    }
}
