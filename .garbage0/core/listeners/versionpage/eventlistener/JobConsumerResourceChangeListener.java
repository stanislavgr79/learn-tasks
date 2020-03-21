package com.epam.aem.core.listeners.versionpage.eventlistener;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.PageManager;
import com.epam.aem.core.listeners.versionpage.CreateVersionPage;
import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.Resource;
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
import javax.jcr.version.VersionManager;

@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "aem/custom/pageEventByResourceChangeListener"
        }
)
@Designate(ocd = JobConsumerResourceChangeListenerConfig.class)
public class JobConsumerResourceChangeListener implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private boolean activateTopic;

    @Activate
    @Modified
    public void activate(JobConsumerResourceChangeListenerConfig jobConsumerResourceChangeListenerConfig) {
        this.activateTopic = jobConsumerResourceChangeListenerConfig.activateTopic();
    }

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Reference
    CreateVersionPage createVersionPage;
    private ResourceResolver resourceResolver;
    private Resource resource;
    private PageManager pageManager = null;
    private Node nodePage = null;
    private VersionManager versionManager = null;

    @Override
    public JobResult process(Job job) {
        if (activateTopic) {
            try {
                workResourceChangeListener(job);
            } catch (RepositoryException e) {
                e.printStackTrace();
                return JobResult.FAILED;
            }
        }
        return JobResult.OK;
    }

    void workResourceChangeListener(Job job) throws RepositoryException {
        String modificationPath = (String) job.getProperty("pageEventByResourceChangeListener");
        if (modificationPath != null){
            executeJob(modificationPath);
        }
    }

    private void executeJob(String modificationPath) throws RepositoryException {
        String pathPage = PathUtils.getParentPath(modificationPath);
        pageManager = getPageManager();
        resource = pageManager.getContainingPage(resourceResolver.getResource(pathPage)).getContentResource(); // (resourceResolver.getResource(modification.getPath()) = JcrNodeResource, type=cq:Page, superType=null, path=/content/learn/us/en
        boolean checkTerms = checkTermsResource();

        if (checkTerms) {
            initEnvironment(pathPage);
            createVersionPage.createNewVersionPage(versionManager, nodePage);
        }
    }

    private void initEnvironment(String pagePath) {
        nodePage = pageManager.getContainingPage(resourceResolver.getResource(pagePath)).getContentResource().adaptTo(Node.class);
        versionManager = getVersionManager(resource);
    }

    private PageManager getPageManager() {
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        return pageManager = resourceResolver.adaptTo(PageManager.class);
    }

    private boolean checkTermsResource() throws RepositoryException {
        boolean isCQ_PAGE = resource.adaptTo(Node.class).getDefinition().getDeclaringNodeType().isNodeType(NameConstants.NT_PAGE);
        boolean hasDescription = resource.getValueMap().containsKey(NameConstants.PN_DESCRIPTION);

        return isCQ_PAGE && hasDescription;
    }

    private VersionManager getVersionManager(Resource resource) {
        try {
            return resource.getResourceResolver().adaptTo(Session.class).getWorkspace().getVersionManager();
        } catch (RepositoryException e) {
            logger.error("Error receiving last version of resource [ {} ]", resource.getName());
        }
        return null;
    }
}
