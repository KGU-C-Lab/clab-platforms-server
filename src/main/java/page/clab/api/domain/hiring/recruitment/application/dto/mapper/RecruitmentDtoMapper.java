package page.clab.api.domain.hiring.recruitment.application.dto.mapper;

import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

public class RecruitmentDtoMapper {

    public static Recruitment toRecruitment(RecruitmentRequestDto requestDto) {
        return Recruitment.builder()
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .applicationType(requestDto.getApplicationType())
                .target(requestDto.getTarget())
                .isDeleted(false)
                .build();
    }

    public static RecruitmentEndDateResponseDto toRecruitmentEndDateResponseDto(Recruitment recruitment) {
        return RecruitmentEndDateResponseDto.builder()
                .id(recruitment.getId())
                .applicationType(recruitment.getApplicationType())
                .build();
    }

    public static RecruitmentResponseDto toRecruitmentResponseDto(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .applicationType(recruitment.getApplicationType())
                .target(recruitment.getTarget())
                .status(recruitment.getStatus().getDescription())
                .updatedAt(recruitment.getUpdatedAt())
                .build();
    }
}
