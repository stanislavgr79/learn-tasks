package com.epam.aem.core.listeners.versionpage.eventhandler;

import com.day.cq.wcm.api.*;
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
import java.util.*;

@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + "aem/custom/pageEventByEventHandler"
        }
)
@Designate(ocd = JobConsumerEventHandlerConfig.class)
public class JobConsumerEventHandler implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private boolean activateTopic;

    @Activate
    @Modified
    public void activate(JobConsumerEventHandlerConfig jobConsumerEventHandlerConfig) {
        this.activateTopic = jobConsumerEventHandlerConfig.activateTopic();
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
                workEventHandler(job);
            } catch (RepositoryException e) {
                e.printStackTrace();
                return JobResult.FAILED;
            }
        }
        return JobResult.OK;
    }

    void workEventHandler(Job job) throws RepositoryException {
        Set<String> propertyNames = job.getPropertyNames();

        if(Objects.equals(propertyNames.stream().filter(o -> o.equals("pageEventByEventHandlerPath")).findAny().orElse(null), "pageEventByEventHandlerPath")){
            createVersionPageByResourcePathModification(job);
        }
        if(Objects.equals(propertyNames.stream().filter(o -> o.equals("pageEventByEventHandler")).findAny().orElse(null), "pageEventByEventHandler")){
            createVersionPageByPageEvent(job);
        }
    }

    private void createVersionPageByResourcePathModification(Job job) throws RepositoryException {
        String path = (String) job.getProperty("pageEventByEventHandlerPath");
        if (path != null){
            executeJob(path);
        }
    }

    private void executeJob(String path) throws RepositoryException {
        String modificationPath = PathUtils.getParentPath(path);
        pageManager = getPageManager();
        resource = pageManager.getContainingPage(resourceResolver.getResource(modificationPath)).getContentResource(); // (resourceResolver.getResource(modification.getPath()) = JcrNodeResource, type=cq:Page, superType=null, path=/content/learn/us/en

        if (checkTermsResource()) {
            initEnvironment(modificationPath);

            GregorianCalendar gregorianCalendar = (GregorianCalendar) resource.getValueMap().get(NameConstants.PN_PAGE_LAST_MOD);
            Long modifiedTime = gregorianCalendar.getTime().getTime();
            Long currentTime = new Date().getTime();

            if(currentTime - modifiedTime < 70L){
                createVersionPage.createNewVersionPage(versionManager, nodePage);
            }
        }
    }

    private void createVersionPageByPageEvent(Job job) throws RepositoryException {
        PageEvent pageEvent = (PageEvent) job.getProperty("pageEventByEventHandler");
        if (pageEvent != null && pageEvent.isLocal()) {
            executeJob(pageEvent);
        }
    }

    private void executeJob(PageEvent pageEvent) throws RepositoryException {
        Iterator<PageModification> modificationsIterator = pageEvent.getModifications();
        while (modificationsIterator.hasNext()) {
            PageModification modification = modificationsIterator.next();
            pageManager = getPageManager();
            resource = pageManager.getContainingPage(resourceResolver.getResource(modification.getPath())).getContentResource(); // (resourceResolver.getResource(modification.getPath()) = JcrNodeResource, type=cq:Page, superType=null, path=/content/learn/us/en

            if (PageModification.ModificationType.MODIFIED.equals(modification.getType()) && checkTermsResource()) {
                initEnvironment(modification.getPath()); //value = /content/learn/us/en
                createVersionPage.createNewVersionPage(versionManager, nodePage);
            }
        }
    }

    private void initEnvironment(String pagePath) {
        nodePage = pageManager.getContainingPage(resourceResolver.getResource(pagePath)).getContentResource().adaptTo(Node.class); // /content/learn/us/en/jcr:content
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
