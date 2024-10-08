package page.clab.api.domain.memberManagement.position.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

import java.util.List;
import java.util.Optional;

public interface RetrievePositionPort {

    Position getById(Long id);

    List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year);

    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);

    Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable);

    List<Position> findByMemberId(String memberId);
}
