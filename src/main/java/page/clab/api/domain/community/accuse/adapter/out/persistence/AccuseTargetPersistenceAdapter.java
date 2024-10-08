package page.clab.api.domain.community.accuse.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccuseTargetPersistenceAdapter implements
        RegisterAccuseTargetPort,
        RetrieveAccuseTargetPort {

    private final AccuseTargetRepository accuseTargetRepository;
    private final AccuseTargetMapper accuseTargetMapper;

    @Override
    public AccuseTarget save(AccuseTarget accuseTarget) {
        AccuseTargetJpaEntity entity = accuseTargetMapper.toEntity(accuseTarget);
        AccuseTargetJpaEntity savedEntity = accuseTargetRepository.save(entity);
        return accuseTargetMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AccuseTarget> findById(AccuseTargetId accuseTargetId) {
        return accuseTargetRepository.findById(accuseTargetId)
                .map(accuseTargetMapper::toDomain);
    }

    @Override
    public AccuseTarget getById(AccuseTargetId accuseTargetId) {
        return accuseTargetRepository.findById(accuseTargetId)
                .map(accuseTargetMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[AccuseTarget] id: " + accuseTargetId + "에 해당하는 신고 대상이 존재하지 않습니다."));
    }

    @Override
    public Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        return accuseTargetRepository.findByConditions(type, status, countOrder, pageable)
                .map(accuseTargetMapper::toDomain);
    }
}
