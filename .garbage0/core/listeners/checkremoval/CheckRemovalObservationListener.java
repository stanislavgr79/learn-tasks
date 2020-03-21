package com.epam.aem.core.listeners.checkremoval;

import org.apache.jackrabbit.api.observation.JackrabbitEvent;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;
import java.util.HashMap;
import java.util.Map;

@Component(
        immediate = true,
        service = EventListener.class
)
public class CheckRemovalObservationListener implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String SERVICE_USER_NAME = "testSystemUser";

    @Reference
    JobManager jobManager;
    @Reference
    SlingRepository repository;
    Session observationSession;

    //not working
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;

    @Activate
    public void activate(ComponentContext context) throws Exception {
        try {
            // Get the JCR Session to bind this Event Listener too
            observationSession = repository.loginAdministrative(null);
//            observationSession = repository.loginService(SERVICE_USER_NAME, null);
//            observationSession = getSession();

            // Get JCR ObservationManager from Workspace
            final ObservationManager observationManager = observationSession.getWorkspace().getObservationManager();

//            // Register the JCR Listener
            observationManager.addEventListener(
                    this, //handler
                    Event.NODE_REMOVED | Event.NODE_MOVED, // events types
//                    "/content/learn/us/*/jcr:content/root", //path
                    "/content/we-retail/", //path
                    true, //isDeep
                    null, //uuids filter
                    null,
                    false
            );

            // Use a single listener registered for multiple path in JCR installer
//            String[] paths = new String[]{"/content/learn/us/", "/content/learn/us/en"};
//            JackrabbitEventFilter eventFilter = new JackrabbitEventFilter()
//                    .setAbsPath(paths[0])
//                    .setEventTypes(Event.NODE_ADDED       |
//                            Event.NODE_REMOVED     |
//                            Event.NODE_MOVED       |
//                            Event.PROPERTY_ADDED   |
//                            Event.PROPERTY_CHANGED |
//                            Event.PROPERTY_REMOVED )
//                    .setIsDeep(true)
//                    .setNoLocal(false)
//                    .setNoExternal(true);
//
//            if (paths.length > 1) {
//                eventFilter.setAdditionalPaths(paths);
//            }
//            JackrabbitObservationManager jackrabbitObservationManager = (JackrabbitObservationManager) observationManager;
//            jackrabbitObservationManager.addEventListener(this, eventFilter);

        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Deactivate
    public void deactivate(final Map<String, String> config) throws RepositoryException {
        try {
            // Get JCR ObservationManager from Workspace
            final ObservationManager observationManager = observationSession.getWorkspace().getObservationManager();

            if (observationManager != null) {
                // Un-register event handler
                observationManager.removeEventListener(this);
            }
        } finally {
            // Good housekeeping; Close your JCR Session when you are done w them!
            if (observationSession != null) {
                observationSession.logout();
            }
        }
    }

    @Override
    public void onEvent(final EventIterator events) {
        try {
            Event event = events.nextEvent();
            // IMPORTANT!
            // JCR Events are NOT cluster-aware and this event listener will be invoked on every node in the cluster.
            // Check if this event was spawned from the server this event handler is running on or from another
            if (event instanceof JackrabbitEvent && ((JackrabbitEvent) event).isExternal()) {
                // Event did NOT originate from this server
                // Skip, Let only the originator process;
                // This is usual to avoid having the same processing happening for every node in a cluster. This
                // is almost always the case when the EventListener modifies the JCR.
                // A possible use-case for handling the event on EVERY member of a cluster would be clearing out an
                // in memory (Service-level) cache.
                return;
            } else {
                // Event originated from THIS server
                // Continue processing this Event
                final String path = event.getPath();
                Map<String, Object> jobProperties = new HashMap<>();
                jobProperties.put("checkRemovalByEventObservation", path);
                jobManager.addJob("aem/custom/pageCheckRemovalByEventObservation", jobProperties);
            }

//            if (Event.NODE_ADDED == event.getType()) {
//                // Node added!
//            } else if (Event.PROPERTY_ADDED == event.getType()) {
//                // Property added!
//            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    //not working
    protected Session getSession() {
        //deprecated
        //Invoke the adaptTo method to create a Session used to create a QueryManager
//        try {
//            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
//        } catch (org.apache.sling.api.resource.LoginException e) {
//            e.printStackTrace();
//        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        return observationSession = resourceResolver.adaptTo(Session.class);
    }
}

