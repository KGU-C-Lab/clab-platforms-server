package page.clab.api.global.common.email.dto.mapper;

import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;

import java.util.List;

public class EmailDtoMapper {

    public static EmailDto toEmailDto(List<String> to, String subject, String content, EmailTemplateType emailTemplateType) {
        return EmailDto.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .emailTemplateType(emailTemplateType)
                .build();
    }
}
