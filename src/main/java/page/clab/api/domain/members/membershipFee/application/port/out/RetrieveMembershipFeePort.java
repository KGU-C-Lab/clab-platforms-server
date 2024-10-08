package page.clab.api.domain.members.membershipFee.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;

public interface RetrieveMembershipFeePort {

    MembershipFee getById(Long id);

    Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable);

    Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}
