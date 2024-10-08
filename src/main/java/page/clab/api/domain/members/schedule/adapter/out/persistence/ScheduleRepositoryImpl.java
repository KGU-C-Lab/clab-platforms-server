package page.clab.api.domain.members.schedule.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.mapper.ScheduleDtoMapper;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.domain.members.schedule.domain.ScheduleType;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ScheduleJpaEntity> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable) {
        QScheduleJpaEntity schedule = QScheduleJpaEntity.scheduleJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        builder.and(schedule.endDateTime.goe(startDateTime))
                .and(schedule.startDateTime.loe(endDateTime));

        if (myGroups != null && !myGroups.isEmpty()) {
            builder.and(schedule.activityGroup.isNull()
                    .or(schedule.activityGroup.in(myGroups)));
        } else {
            builder.and(schedule.activityGroup.isNull());
        }

        List<ScheduleJpaEntity> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, schedule))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<ScheduleJpaEntity> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        QScheduleJpaEntity schedule = QScheduleJpaEntity.scheduleJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (year != null) {
            builder.and(schedule.startDateTime.year().eq(year));
        }
        if (month != null) {
            builder.and(schedule.startDateTime.month().eq(month));
        }
        if (priority != null) {
            builder.and(schedule.priority.eq(priority));
        }

        List<ScheduleJpaEntity> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, schedule))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<ScheduleJpaEntity> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate, String memberId, Pageable pageable) {
        QScheduleJpaEntity schedule = QScheduleJpaEntity.scheduleJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        builder.and(schedule.endDateTime.goe(startDateTime))
                .and(schedule.startDateTime.loe(endDateTime))
                .and(schedule.scheduleWriter.eq(memberId))
                .and(schedule.scheduleType.ne(ScheduleType.ALL));

        List<ScheduleJpaEntity> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, schedule))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public ScheduleCollectResponseDto findCollectSchedules() {
        QScheduleJpaEntity schedule = QScheduleJpaEntity.scheduleJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()).atTime(23, 59, 59);

        builder.and(schedule.startDateTime.goe(startDateTime))
                .and(schedule.endDateTime.loe(endDateTime));

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        builder.and(schedule.priority.eq(SchedulePriority.HIGH));

        long highPriorityCount = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return ScheduleDtoMapper.toScheduleCollectResponseDto(total, highPriorityCount);
    }
}
