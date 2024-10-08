package page.clab.api.domain.hiring.recruitment.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecruitmentPersistenceAdapter implements
        RegisterRecruitmentPort,
        UpdateRecruitmentPort,
        RetrieveRecruitmentPort {

    private final RecruitmentRepository repository;
    private final RecruitmentMapper mapper;

    @Override
    public Recruitment save(Recruitment recruitment) {
        RecruitmentJpaEntity entity = mapper.toEntity(recruitment);
        RecruitmentJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Recruitment update(Recruitment recruitment) {
        RecruitmentJpaEntity entity = mapper.toEntity(recruitment);
        RecruitmentJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public Recruitment getById(Long recruitmentId) {
        return repository.findById(recruitmentId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Recruitment] id: " + recruitmentId + "에 해당하는 모집 공고가 존재하지 않습니다."));
    }

    @Override
    public List<Recruitment> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Recruitment> findTop5ByOrderByCreatedAtDesc() {
        return repository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Recruitment> findByEndDateBetween(LocalDateTime weekAgo, LocalDateTime now) {
        return repository.findByEndDateBetween(weekAgo, now).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
