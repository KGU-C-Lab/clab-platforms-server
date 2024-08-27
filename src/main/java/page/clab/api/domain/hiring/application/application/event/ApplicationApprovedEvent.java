package page.clab.api.domain.hiring.application.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.hiring.application.domain.Application;

@Getter
public class ApplicationApprovedEvent extends ApplicationEvent {

    private final Application application;

    public ApplicationApprovedEvent(Object source, Application application) {
        super(source);
        this.application = application;
    }
}
