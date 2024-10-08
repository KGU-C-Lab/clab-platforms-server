package page.clab.api.domain.community.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.jobPosting.application.dto.mapper.JobPostingDtoMapper;
import page.clab.api.domain.community.jobPosting.application.dto.response.JobPostingResponseDto;
import page.clab.api.domain.community.jobPosting.application.port.in.RetrieveJobPostingsByConditionsUseCase;
import page.clab.api.domain.community.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class JobPostingsByConditionsRetrievalService implements RetrieveJobPostingsByConditionsUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<JobPostingResponseDto> retrieveJobPostings(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        Page<JobPosting> jobPostings = retrieveJobPostingPort.findByConditions(title, companyName, careerLevel, employmentType, pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingDtoMapper::toJobPostingResponseDto));
    }
}
