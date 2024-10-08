package page.clab.api.domain.members.schedule.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.schedule.domain.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleJpaEntity toEntity(Schedule schedule);

    Schedule toDomain(ScheduleJpaEntity entity);
}
