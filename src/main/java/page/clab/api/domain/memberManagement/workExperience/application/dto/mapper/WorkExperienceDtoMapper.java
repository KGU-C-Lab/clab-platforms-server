package page.clab.api.domain.memberManagement.workExperience.application.dto.mapper;

import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

public class WorkExperienceDtoMapper {

    public static WorkExperience toWorkExperience(WorkExperienceRequestDto requestDto, String memberId) {
        return WorkExperience.builder()
                .companyName(requestDto.getCompanyName())
                .position(requestDto.getPosition())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .memberId(memberId)
                .isDeleted(false)
                .build();
    }

    public static WorkExperienceResponseDto toWorkExperienceResponseDto(WorkExperience workExperience) {
        return WorkExperienceResponseDto.builder()
                .id(workExperience.getId())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .startDate(workExperience.getStartDate())
                .endDate(workExperience.getEndDate())
                .build();
    }
}
