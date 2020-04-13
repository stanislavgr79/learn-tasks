package com.epam.aem.core.tasks.task1.listener.nodedelete;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "aem/custom/checkTableByEventObservation"
        }
)
@Designate(ocd = JobCheckTableObservationListenerConfig.class)
public class JobCheckTableObservationListener implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "system_user";
    private boolean activateTopic;

    @Activate
    @Modified
    public void activate(JobCheckTableObservationListenerConfig jobCheckTableObservationListenerConfig) {
        this.activateTopic = jobCheckTableObservationListenerConfig.activateTopic();
    }

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;

    @Override
    public JobResult process(Job job) {
        if (activateTopic) {
            try {
                workJobByEventOfCheckRemovalObservationListener(job);
            } catch (RepositoryException e) {
                logger.error(String.format("JobResult process: %s", e.getMessage()));
                return JobResult.FAILED;
            }
        }
        return JobResult.OK;
    }

    void workJobByEventOfCheckRemovalObservationListener(Job job) throws RepositoryException {
        String modificationPath = (String) job.getProperty("checkTableByEventObservation");

        if (modificationPath != null) {
            resourceResolver = getResourceResolverFromResolverFactoryByUser();
            Session session = resourceResolver.adaptTo(Session.class);

            Node node = resourceResolver.getResource(modificationPath).adaptTo(Node.class);

            Objects.requireNonNull(node).remove();
            Objects.requireNonNull(session).save();
            session.logout();
        }
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
