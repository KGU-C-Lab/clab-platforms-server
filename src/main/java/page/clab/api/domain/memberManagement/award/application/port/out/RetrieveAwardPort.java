package page.clab.api.domain.memberManagement.award.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.award.domain.Award;

import java.util.List;

public interface RetrieveAwardPort {

    Award getById(Long awardId);

    Page<Award> findByConditions(String memberId, Long year, Pageable pageable);

    Page<Award> findAllByIsDeletedTrue(Pageable pageable);

    Page<Award> findByMemberId(String memberId, Pageable pageable);

    List<Award> findByMemberId(String memberId);
}
