package com.epam.aem.core.tasks.task5.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EventComponent {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private String description;
    @ValueMapValue
    private Date eventStartDate;
    @ValueMapValue
    private String place;
    @ValueMapValue
    private String topic;

    public EventComponent() {
    }

    public EventComponent(String title, String description, Date eventStartDate, String place, String topic) {
        this.title = title;
        this.description = description;
        this.eventStartDate = eventStartDate;
        this.place = place;
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public String getPlace() {
        return place;
    }

    public String getTopic() {
        return topic;
    }
}
