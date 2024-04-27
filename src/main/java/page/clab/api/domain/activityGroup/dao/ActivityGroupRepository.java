package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.award.domain.Award;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long>, ActivityGroupRepositoryCustom, QuerydslPredicateExecutor<ActivityGroup> {

    Page<ActivityGroup> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityGroup> findAllByCategoryOrderByCreatedAtDesc(ActivityGroupCategory category, Pageable pageable);

    @Query(value = "SELECT a.* FROM activity_group a WHERE a.is_deleted = true", nativeQuery = true)
    Page<ActivityGroup> findAllByIsDeletedTrue(Pageable pageable);

}
