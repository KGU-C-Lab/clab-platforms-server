package page.clab.api.domain.community.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.jobPosting.application.dto.mapper.JobPostingDtoMapper;
import page.clab.api.domain.community.jobPosting.application.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.community.jobPosting.application.port.in.RetrieveJobPostingDetailsUseCase;
import page.clab.api.domain.community.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

@Service
@RequiredArgsConstructor
public class JobPostingDetailsRetrievalService implements RetrieveJobPostingDetailsUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional(readOnly = true)
    @Override
    public JobPostingDetailsResponseDto retrieveJobPostingDetails(Long jobPostingId) {
        JobPosting jobPosting = retrieveJobPostingPort.getById(jobPostingId);
        return JobPostingDtoMapper.toJobPostingDetailsResponseDto(jobPosting);
    }
}
