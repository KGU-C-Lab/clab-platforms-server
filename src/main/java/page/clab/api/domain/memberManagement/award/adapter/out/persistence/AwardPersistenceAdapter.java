package page.clab.api.domain.memberManagement.award.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.memberManagement.award.application.port.out.RemoveAwardPort;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AwardPersistenceAdapter implements
        RegisterAwardPort,
        RetrieveAwardPort,
        RemoveAwardPort {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    @Override
    public Award save(Award award) {
        AwardJpaEntity entity = awardMapper.toEntity(award);
        AwardJpaEntity savedEntity = awardRepository.save(entity);
        return awardMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Award> awards) {
        List<AwardJpaEntity> entities = awards.stream()
                .map(awardMapper::toEntity)
                .toList();
        awardRepository.saveAll(entities);
    }

    @Override
    public void delete(Award award) {
        AwardJpaEntity entity = awardMapper.toEntity(award);
        awardRepository.delete(entity);
    }

    @Override
    public Award getById(Long awardId) {
        return awardRepository.findById(awardId)
                .map(awardMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Award] id: " + awardId + "에 해당하는 수상 이력이 존재하지 않습니다."));
    }

    @Override
    public Page<Award> findByConditions(String memberId, Long year, Pageable pageable) {
        return awardRepository.findByConditions(memberId, year, pageable)
                .map(awardMapper::toDomain);
    }

    @Override
    public Page<Award> findAllByIsDeletedTrue(Pageable pageable) {
        return awardRepository.findAllByIsDeletedTrue(pageable)
                .map(awardMapper::toDomain);
    }

    @Override
    public Page<Award> findByMemberId(String memberId, Pageable pageable) {
        return awardRepository.findByMemberId(memberId, pageable)
                .map(awardMapper::toDomain);
    }

    @Override
    public List<Award> findByMemberId(String memberId) {
        return awardRepository.findByMemberId(memberId).stream()
                .map(awardMapper::toDomain)
                .toList();
    }
}
