package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class FetchJobPostingDetailsServiceImpl implements FetchJobPostingDetailsService {

    private final JobPostingRepository jobPostingRepository;

    @Transactional(readOnly = true)
    @Override
    public JobPostingDetailsResponseDto execute(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        return JobPostingDetailsResponseDto.toDto(jobPosting);
    }

    private JobPosting getJobPostingByIdOrThrow(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채용 공고입니다."));
    }
}
