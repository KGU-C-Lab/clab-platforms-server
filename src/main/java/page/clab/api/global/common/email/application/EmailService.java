package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.mapper.EmailDtoMapper;
import page.clab.api.global.common.email.dto.request.EmailDto;
import page.clab.api.global.common.email.exception.MessageSendingFailedException;
import page.clab.api.global.config.EmailTemplateProperties;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailAsyncService emailAsyncService;
    private final EmailTemplateProperties emailTemplateProperties;
    private final SpringTemplateEngine springTemplateEngine;

    public void sendAccountCreationEmail(Member member, String password) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.ACCOUNT_CREATION);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", password);

        EmailDto emailDto = EmailDtoMapper.toEmailDto(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.ACCOUNT_CREATION
        );

        sendEmail(emailDto, member, " 계정 발급 안내 메일 전송에 실패했습니다.");
    }

    public void sendPasswordResetCodeEmail(Member member, String code) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.PASSWORD_RESET_CODE);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{code}}", code);

        EmailDto emailDto = EmailDtoMapper.toEmailDto(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.PASSWORD_RESET_CODE
        );

        sendEmail(emailDto, member, " 비밀번호 재발급 인증 메일 전송에 실패했습니다.");
    }

    public void sendNewPasswordEmail(Member member, String newPassword) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.NEW_PASSWORD);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", newPassword);

        EmailDto emailDto = EmailDtoMapper.toEmailDto(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.NEW_PASSWORD
        );

        sendEmail(emailDto, member, " 비밀번호 재설정 안내 메일 전송에 실패했습니다.");
    }

    private void sendEmail(EmailDto emailDto, Member member, String message) {
        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new MessageSendingFailedException(member.getEmail() + message);
        }
    }

    private String generateEmailContent(EmailDto emailDto, String name) {
        Context context = new Context();
        context.setVariable("title", emailDto.getSubject());
        context.setVariable("name", name);
        context.setVariable("content", emailDto.getContent());

        String emailTemplate = emailDto.getEmailTemplateType().getTemplateName();
        return springTemplateEngine.process(emailTemplate, context);
    }
}
