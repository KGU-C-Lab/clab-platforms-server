package page.clab.api.domain.memberManagement.position.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.application.port.out.UpdatePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PositionPersistenceAdapter implements
        RegisterPositionPort,
        RetrievePositionPort,
        UpdatePositionPort {

    private final PositionRepository repository;
    private final PositionMapper mapper;

    @Override
    public Position save(Position position) {
        PositionJpaEntity entity = mapper.toEntity(position);
        PositionJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Position> positions) {
        List<PositionJpaEntity> entities = positions.stream()
                .map(mapper::toEntity)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    public Position update(Position position) {
        PositionJpaEntity entity = mapper.toEntity(position);
        PositionJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public Position getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Position] id: " + id + "에 해당하는 직책이 존재하지 않습니다."));
    }

    @Override
    public Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType) {
        return repository.findByMemberIdAndYearAndPositionType(memberId, year, positionType)
                .map(mapper::toDomain);
    }

    @Override
    public List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year) {
        return repository.findAllByMemberIdAndYearOrderByPositionTypeAsc(memberId, year).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable) {
        return repository.findByConditions(year, positionType, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Position> findByMemberId(String memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
