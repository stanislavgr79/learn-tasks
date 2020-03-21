package com.epam.aem.core.tasks.task1.listener.propertychange;

import org.apache.jackrabbit.oak.commons.PathUtils;
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
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "aem/custom/changePropertyTableByEventObservation"
        }
)
@Designate(ocd = JobChangeTableByPropertyObservationListenerConfig.class)
public class JobChangeTableByPropertyObservationListener implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "testSystemUser";
    private boolean activateTopic;

    @Activate
    @Modified
    public void activate(JobChangeTableByPropertyObservationListenerConfig jobChangeTableByPropertyObservationListenerConfig) {
        this.activateTopic = jobChangeTableByPropertyObservationListenerConfig.activateTopic();
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
                logger.error(String.format("process: %s", e.getMessage()));
                return JobResult.FAILED;
            }
        }
        return JobResult.OK;
    }

    private int row;
    private int column;

    void workJobByEventOfCheckRemovalObservationListener(Job job) throws RepositoryException {
        String modificationPath = (String) job.getProperty("changePropertyTableByEventObservation");

        if (modificationPath != null) {
            resourceResolver = getResourceResolverFromResolverFactoryByUser();
            Session session = resourceResolver.adaptTo(Session.class);

            Node node = Objects.requireNonNull(resourceResolver.getResource(modificationPath)).adaptTo(Node.class);

            List<Node> removeNodesList = new ArrayList<>();

            row = (int) Objects.requireNonNull(node).getProperty("row").getValue().getLong();
            column = (int) node.getProperty("column").getValue().getLong();

            NodeIterator nodeIterator = node.getNodes();
            while (nodeIterator.hasNext()) {
                Node checkNode = nodeIterator.nextNode();
                if (checkNodeForRemove(checkNode)) {
                    removeNodesList.add(checkNode);
                }
            }

            for (Node removeNode : removeNodesList) {
                removeNode.remove();
            }
            Objects.requireNonNull(session).save();
            session.logout();
        }
    }

    private boolean checkNodeForRemove(Node checkNode) throws RepositoryException {
        int nodeRow = 0;
        int nodeColumn = 0;
        String nameNodeChild = PathUtils.getName(checkNode.getPath());
        nodeRow = Integer.parseInt(nameNodeChild.substring(nameNodeChild.indexOf("_r") + 2, nameNodeChild.indexOf("_c")));
        nodeColumn = Integer.parseInt(nameNodeChild.substring(nameNodeChild.indexOf("_c") + 2));

        return nodeRow > row || nodeColumn > column;
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
