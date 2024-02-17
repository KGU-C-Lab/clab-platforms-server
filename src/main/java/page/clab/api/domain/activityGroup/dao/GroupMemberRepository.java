package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberId;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    Optional<GroupMember> findByMember(Member member);

    List<GroupMember> findAllByMember(Member member);

    boolean existsByMemberAndActivityGroupId(Member member, Long activityGroupId);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role);

   Optional<GroupMember> findByMemberAndActivityGroup(Member member, ActivityGroup activityGroup);

    List<GroupMember> findAllByActivityGroupIdOrderByMember_IdAsc(Long activityGroupId);

    Page<GroupMember> findAllByActivityGroupIdOrderByMember_IdAsc(Long activityGroupId, Pageable pageable);

    Page<GroupMember> findAllByActivityGroupIdAndStatusOrderByMember_IdAsc(Long activityGroupId, GroupMemberStatus status, org.springframework.data.domain.Pageable pageable);

}
