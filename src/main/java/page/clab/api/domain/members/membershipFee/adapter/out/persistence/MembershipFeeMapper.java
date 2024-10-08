package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;

@Mapper(componentModel = "spring")
public interface MembershipFeeMapper {

    MembershipFeeJpaEntity toEntity(MembershipFee membershipFee);

    MembershipFee toDomain(MembershipFeeJpaEntity jpaEntity);
}
