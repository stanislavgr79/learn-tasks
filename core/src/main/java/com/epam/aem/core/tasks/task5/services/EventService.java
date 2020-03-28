package com.epam.aem.core.tasks.task5.services;

import com.epam.aem.core.tasks.task5.models.EventComponent;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.Map;

public interface EventService {

    Map<Integer, List<EventComponent>> getAllEvents(String pathRequest, int currentTablePage) throws RepositoryException;
    EventComponent getEvent(String event) throws RepositoryException;

}
