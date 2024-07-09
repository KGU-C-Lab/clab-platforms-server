package page.clab.api.domain.jobPosting.application.port.in;

import page.clab.api.domain.jobPosting.application.dto.response.JobPostingDetailsResponseDto;

public interface RetrieveJobPostingDetailsUseCase {
    JobPostingDetailsResponseDto retrieveJobPostingDetails(Long jobPostingId);
}
