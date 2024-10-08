package page.clab.api.domain.memberManagement.member.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;

import java.util.List;
import java.util.Optional;

public interface RetrieveMemberPort {

    Optional<Member> findById(String memberId);

    Member getById(String memberId);

    List<Member> findAll();

    Page<Member> findMemberRoleInfoByConditions(String memberId, String memberName, Role role, Pageable pageable);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Member getByEmail(String email);

    Page<Member> findBirthdaysThisMonth(int month, Pageable pageable);

    Page<Member> findByConditions(String id, String name, Pageable pageable);

    Member getFirstByRole(Role role);
}
