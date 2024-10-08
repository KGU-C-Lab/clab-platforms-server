package page.clab.api.domain.memberManagement.workExperience.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

import java.util.List;

public interface RetrieveWorkExperiencePort {

    WorkExperience getById(Long id);

    Page<WorkExperience> findAllByIsDeletedTrue(Pageable pageable);

    List<WorkExperience> findByMemberId(String memberId);

    Page<WorkExperience> findByMemberId(String memberId, Pageable pageable);

    Page<WorkExperience> findByConditions(String memberId, Pageable pageable);
}
