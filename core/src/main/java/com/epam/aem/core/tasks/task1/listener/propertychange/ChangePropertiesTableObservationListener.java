package com.epam.aem.core.tasks.task1.listener.propertychange;

import org.apache.jackrabbit.api.observation.JackrabbitEvent;
import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component(
        immediate = true,
        service = EventListener.class
)
public class ChangePropertiesTableObservationListener implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "system_user";

    @Reference
    JobManager jobManager;
    Session observationSession;

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;

    @Activate
    public void activate(ComponentContext context){
        try {
            final String[] nodeTypes = {"nt:unstructured"};
            resourceResolver = getResourceResolverFromResolverFactoryByUser();
            observationSession = resourceResolver.adaptTo(Session.class);
            final ObservationManager observationManager = Objects.requireNonNull(observationSession).getWorkspace().getObservationManager();

            observationManager.addEventListener(
                    this, //handler
                    Event.PROPERTY_CHANGED, // events types
//                    "/content/learn/us/*/jcr:content/root", //path
                    "/content/we-retail/", //path
                    true, //isDeep
                    null, //uuids filter
                    nodeTypes,
                    false
            );
        } catch (RepositoryException e) {
            logger.error(String.format("activate: %s", e.getMessage()));
        }
    }

    @Deactivate
    public void deactivate(final Map<String, String> config) throws RepositoryException {
        try {
            final ObservationManager observationManager = observationSession.getWorkspace().getObservationManager();
            if (observationManager != null) {
                observationManager.removeEventListener(this);
            }
        } finally {
            if (observationSession != null) {
                observationSession.logout();
            }
        }
    }

    @Override
    public void onEvent(final EventIterator events) {
        try {
            while (events.hasNext()) {
                Event event = events.nextEvent();
                if (event instanceof JackrabbitEvent && ((JackrabbitEvent) event).isExternal()) {
                    return;
                } else {
                    // Event originated from THIS server Continue processing this Event
                    if (checkEventForCorrectResourceType(event)) {
                        final String path = event.getPath();
                        String parentNodePath = PathUtils.getAncestorPath(path, 1);
                        Map<String, Object> jobProperties = new HashMap<>();
                        jobProperties.put("changePropertyTableByEventObservation", parentNodePath);
                        jobManager.addJob("aem/custom/changePropertyTableByEventObservation", jobProperties);
                        return;
                    }
                }
            }
        } catch (RepositoryException e) {
            logger.error(String.format("onEvent: %s", e.getMessage()));
        }
    }

    private boolean checkEventForCorrectResourceType(Event event) throws RepositoryException {
        String modificationPathName = PathUtils.getName(event.getPath());
        String nodePath = event.getIdentifier();
        if (observationSession.nodeExists(nodePath)) {
            Node node = observationSession.getNode(nodePath);
            return node.getProperty("sling:resourceType").getValue().getString().equals("learn/components/tasks/parsystabletask1") &&
                    (modificationPathName.equals("column") || modificationPathName.equals("row"));
        }
        return false;
    }

    protected ResourceResolver getResourceResolverFromResolverFactoryByUser() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            logger.error(String.format("getResourceResolverFromResolverFactoryByUser: %s", e.getMessage()));
        }
        return resourceResolver;
    }
}

