package page.clab.api.domain.memberManagement.award.application.dto.mapper;

import page.clab.api.domain.memberManagement.award.application.dto.request.AwardRequestDto;
import page.clab.api.domain.memberManagement.award.application.dto.response.AwardResponseDto;
import page.clab.api.domain.memberManagement.award.domain.Award;

public class AwardDtoMapper {

    public static Award toAward(AwardRequestDto requestDto, String memberId) {
        return Award.builder()
                .competitionName(requestDto.getCompetitionName())
                .organizer(requestDto.getOrganizer())
                .awardName(requestDto.getAwardName())
                .awardDate(requestDto.getAwardDate())
                .memberId(memberId)
                .isDeleted(false)
                .build();
    }

    public static AwardResponseDto toAwardResponseDto(Award award) {
        return AwardResponseDto.builder()
                .id(award.getId())
                .competitionName(award.getCompetitionName())
                .organizer(award.getOrganizer())
                .awardName(award.getAwardName())
                .awardDate(award.getAwardDate())
                .build();
    }
}
