package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class JobPostingPersistenceAdapter implements
        RetrieveJobPostingPort {

    private final JobPostingRepository repository;
    private final JobPostingMapper jobPostingMapper;

    @Override
    public JobPosting getById(Long jobPostingId) {
        return repository.findById(jobPostingId)
                .map(jobPostingMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[JobPosting] id: " + jobPostingId + "에 해당하는 채용 공고가 존재하지 않습니다."));
    }

    @Override
    public Page<JobPosting> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        return repository.findByConditions(title, companyName, careerLevel, employmentType, pageable).map(jobPostingMapper::toDomain);
    }
}
