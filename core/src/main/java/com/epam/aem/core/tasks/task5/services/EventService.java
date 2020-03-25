package com.epam.aem.core.tasks.task5.services;

import com.epam.aem.core.tasks.task5.models.EventComponent;

import javax.jcr.RepositoryException;
import java.util.List;

public interface EventService {

    List<EventComponent> getAllEvents(String pathRequest) throws RepositoryException;
    EventComponent getEvent(String event) throws RepositoryException;

}
