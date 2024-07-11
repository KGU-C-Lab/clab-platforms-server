package page.clab.api.domain.accuse.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.accuse.application.port.out.RegisterAccusePort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.accuse.application.port.out.UpdateAccusePort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccusePersistenceAdapter implements
        RegisterAccusePort,
        RetrieveAccusePort,
        UpdateAccusePort {

    private final AccuseRepository accuseRepository;
    private final AccuseMapper accuseMapper;

    @Override
    public Accuse save(Accuse accuse) {
        AccuseJpaEntity entity = accuseMapper.toJpaEntity(accuse);
        AccuseJpaEntity savedEntity = accuseRepository.save(entity);
        return accuseMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Accuse> accuses) {
        List<AccuseJpaEntity> entities = accuses.stream()
                .map(accuseMapper::toJpaEntity)
                .toList();
        accuseRepository.saveAll(entities);
    }

    @Override
    public Optional<Accuse> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByMemberIdAndTarget(memberId, targetType, targetReferenceId)
                .map(accuseMapper::toDomain);
    }

    @Override
    public List<Accuse> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByTargetOrderByCreatedAtDesc(targetType, targetReferenceId).stream()
                .map(accuseMapper::toDomain)
                .toList();
    }

    @Override
    public List<Accuse> findByTarget(TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByTarget(targetType, targetReferenceId).stream()
                .map(accuseMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Accuse> findByMemberId(String memberId, Pageable pageable) {
        return accuseRepository.findByMemberId(memberId, pageable)
                .map(accuseMapper::toDomain);
    }

    @Override
    public List<Accuse> findByMemberId(String memberId) {
        return accuseRepository.findByMemberId(memberId).stream()
                .map(accuseMapper::toDomain)
                .toList();
    }

    @Override
    public Accuse update(Accuse accuse) {
        AccuseJpaEntity entity = accuseMapper.toJpaEntity(accuse);
        AccuseJpaEntity updatedEntity = accuseRepository.save(entity);
        return accuseMapper.toDomain(updatedEntity);
    }
}
