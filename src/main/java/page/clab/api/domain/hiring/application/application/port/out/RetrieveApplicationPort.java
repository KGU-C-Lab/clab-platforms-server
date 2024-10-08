package page.clab.api.domain.hiring.application.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.hiring.application.adapter.out.persistence.ApplicationId;
import page.clab.api.domain.hiring.application.domain.Application;

import java.util.List;
import java.util.Optional;

public interface RetrieveApplicationPort {

    Optional<Application> findById(ApplicationId applicationId);

    Application getById(ApplicationId applicationId);

    Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);

    List<Application> findByRecruitmentIdAndIsPass(Long recruitmentId, boolean isPass);

    Application getByRecruitmentIdAndStudentId(Long recruitmentId, String studentId);
}
