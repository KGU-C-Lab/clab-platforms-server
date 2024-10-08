package page.clab.api.external.memberManagement.member.application.port;

import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberReviewInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ExternalRetrieveMemberUseCase {

    boolean existsById(String memberId);

    void ensureMemberExists(String memberId);

    Optional<Member> findById(String id);

    Member getById(String memberId);

    Member findByEmail(String address);

    Member getCurrentMember();

    String getCurrentMemberId();

    List<MemberEmailInfoDto> getMembers();

    List<String> getMemberIds();

    List<String> getAdminIds();

    List<String> getSuperAdminIds();

    MemberBasicInfoDto getMemberBasicInfoById(String memberId);

    MemberBasicInfoDto getCurrentMemberBasicInfo();

    MemberDetailedInfoDto getMemberDetailedInfoById(String memberId);

    MemberDetailedInfoDto getCurrentMemberDetailedInfo();

    MemberBorrowerInfoDto getCurrentMemberBorrowerInfo();

    MemberLoginInfoDto getMemberLoginInfoById(String memberId);

    MemberLoginInfoDto getGuestMemberLoginInfo();

    MemberPositionInfoDto getCurrentMemberPositionInfo();

    MemberReviewInfoDto getMemberReviewInfoById(String memberId);
}
