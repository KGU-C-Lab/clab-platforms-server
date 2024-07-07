package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;

import java.util.List;

@Repository
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
    List<GroupSchedule> findAllByActivityGroupIdOrderByIdDesc(Long activityGroupId);

    Page<GroupSchedule> findAllByActivityGroupId(Long activityGroupId, Pageable pageable);
}
