package com.epam.aem.core.listeners.checkremoval;

import com.day.cq.wcm.api.NameConstants;
import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.jcr.api.SlingRepository;
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

@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "aem/custom/pageCheckRemovalByEventObservation"
        }
)
@Designate(ocd = JobCheckRemovalObservationListenerConfig.class)
public class JobCheckRemovalObservationListener implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String SERVICE_USER_NAME = "testSystemUser";
    private boolean activateTopic;

    String PATH_WHERE_NEED_CREATE_NODES = "/var/log/removedProperties";

    @Activate
    @Modified
    public void activate(JobCheckRemovalObservationListenerConfig jobConsumerObservationListenerConfig) {
        this.activateTopic = jobConsumerObservationListenerConfig.activateTopic();
    }

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    SlingRepository slingRepository;

    @Override
    public JobResult process(Job job) {
        if (activateTopic) {
            try {
                workJobByEventOfCheckRemovalObservationListener(job);
            } catch (RepositoryException e) {
                e.printStackTrace();
                return JobResult.FAILED;
            }
        }
        return JobResult.OK;
    }

    void workJobByEventOfCheckRemovalObservationListener(Job job) throws RepositoryException {
        String modificationPath = (String) job.getProperty("checkRemovalByEventObservation");

        if (modificationPath != null) {

            String nameRemovalNode = PathUtils.getName(modificationPath);
            String pathRemovalNode = PathUtils.getAncestorPath(modificationPath, 1);

            //work !!!!!!!!
            Session slingSessionByAdmin = slingRepository.loginAdministrative(null).getWorkspace().getSession();
            // WORK !!!!!!! FROM factory to resourceResolver by admin = adaptTo session
//            ResourceResolver resourceResolverByAdmin = getResourceResolverFromResolverFactoryByAdmin();
//            Node nodeFromResourceResolverByAdminLogin = resourceResolverByAdmin.getResource(PATH_WHERE_NEED_CREATE_NODES).adaptTo(Node.class);
//            Node fromSessionAdmin = resourceResolverByAdmin.adaptTo(Session.class).getWorkspace().getSession().getNode(PATH_WHERE_NEED_CREATE_NODES);


            ///////////////////////////////////////////////////////

            Session slingSessionByUser = slingRepository.loginService(SERVICE_USER_NAME, null);
            //------>path not found
            Node nodeFromSlingSessionByUser = slingRepository.loginService(SERVICE_USER_NAME, null).getWorkspace().getSession().getNode(PATH_WHERE_NEED_CREATE_NODES);

            //// from factory to resourceResolver by user = adaptTo session
            ResourceResolver resourceResolverByUser = getResourceResolverFromResolverFactoryByUser();
            //-------->null
            Node nodeFromResourceResolverByUserLogin = resourceResolverByUser.getResource(PATH_WHERE_NEED_CREATE_NODES).adaptTo(Node.class);
            //-------->pathNotFound
            Session session = resourceResolverByUser.adaptTo(Session.class).getWorkspace().getSession();
            Node fromSessionUser = session.getNode(PATH_WHERE_NEED_CREATE_NODES);

            /////////////////////////////////////

//            Node slingSessionNodeByAdmin = slingSessionByAdmin.getNode(PATH_WHERE_NEED_CREATE_NODES);
            if (fromSessionUser != null){
                Node new_Node = fromSessionUser.addNode(nameRemovalNode+"_"+"1", NameConstants.NT_COMPONENT);
                new_Node.setProperty("propertyName" , nameRemovalNode);
                new_Node.setProperty("propertyPath" , pathRemovalNode);
            }

            session.save();
            session.logout();
        }
    }

    protected ResourceResolver getResourceResolverFromResolverFactoryByUser() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        return resourceResolver;
    }

    protected ResourceResolver getResourceResolverFromResolverFactoryByAdmin() {
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        return resourceResolver;
    }
}
