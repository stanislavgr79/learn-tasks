package com.epam.aem.core.tasks.task1.listener.nodedelete;

import org.apache.jackrabbit.api.observation.JackrabbitEvent;
import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.Resource;
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
public class CheckTableForDeleteNodeListener implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "testSystemUser";

    @Reference
    JobManager jobManager;
    Session observationSession;

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;

    @Activate
    public void activate(ComponentContext context) {
        try {
            final String[] nodeTypes = {"nt:unstructured"};

            resourceResolver = getResourceResolverFromResolverFactoryByUser();
            observationSession = resourceResolver.adaptTo(Session.class);

            final ObservationManager observationManager = Objects.requireNonNull(observationSession).getWorkspace().getObservationManager();

            observationManager.addEventListener(
                    this, //handler
                    Event.NODE_REMOVED, // events types
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
            Event event = events.nextEvent();
            if (!(event instanceof JackrabbitEvent && ((JackrabbitEvent) event).isExternal())) {
                // Event originated from THIS server Continue processing this Event
                final String path = event.getPath();
                String parentNodePath = PathUtils.getAncestorPath(path, 1);
                if (checkRemovalNodeFromTableBySession(parentNodePath) && checkRemovalNodeFromTableByResource(parentNodePath)) {
                    Map<String, Object> jobProperties = new HashMap<>();
                    jobProperties.put("checkTableByEventObservation", parentNodePath);
                    jobManager.addJob("aem/custom/checkTableByEventObservation", jobProperties);
                }
            }
        } catch (RepositoryException e) {
            logger.error(String.format("onEvent: %s", e.getMessage()));
        }
    }

    private boolean checkRemovalNodeFromTableBySession(String parentNodePath) throws RepositoryException {
        if (observationSession.nodeExists(parentNodePath)) {
            Node parentNode = observationSession.getNode(parentNodePath);
            if (!parentNode.hasNodes()) {
                return parentNode.getParent().getProperty("sling:resourceType")
                        .getValue().getString().equals("learn/components/tasks/parsystabletask1");
            }
        }
        return false;
    }

    private boolean checkRemovalNodeFromTableByResource(String parentNodePath){
        Resource resource = resourceResolver.getResource(parentNodePath);
        if (!(resource != null && resource.hasChildren())) {
            return Objects.requireNonNull(Objects.requireNonNull(resource).getParent()).isResourceType("learn/components/tasks/parsystabletask1");
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
            logger.error(String.format("onEvent: %s", e.getMessage()));
        }
        return resourceResolver;
    }
}

