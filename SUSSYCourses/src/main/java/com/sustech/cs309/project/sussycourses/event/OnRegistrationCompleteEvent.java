package com.sustech.cs309.project.sussycourses.event;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final WebAppUser webAppUser;

    public OnRegistrationCompleteEvent(WebAppUser webAppUser) {
        super(webAppUser);
        this.webAppUser = webAppUser;
    }

    public WebAppUser getUser() {
        return webAppUser;
    }
}
