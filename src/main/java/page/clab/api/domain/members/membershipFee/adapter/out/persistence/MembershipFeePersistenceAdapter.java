package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.UpdateMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class MembershipFeePersistenceAdapter implements
        RegisterMembershipFeePort,
        UpdateMembershipFeePort,
        RetrieveMembershipFeePort {

    private final MembershipFeeRepository repository;
    private final MembershipFeeMapper mapper;

    @Override
    public MembershipFee getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

    @Override
    public MembershipFee save(MembershipFee membershipFee) {
        MembershipFeeJpaEntity entity = mapper.toEntity(membershipFee);
        MembershipFeeJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public MembershipFee update(MembershipFee membershipFee) {
        MembershipFeeJpaEntity entity = mapper.toEntity(membershipFee);
        MembershipFeeJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, category, status, pageable)
                .map(mapper::toDomain);
    }
}
