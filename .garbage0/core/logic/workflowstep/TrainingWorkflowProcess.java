package com.epam.aem.core.logic.workflowstep;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.HashMap;
import java.util.Map;

@Component(service = WorkflowProcess.class,
        property = {
                "process.label" + "=" + "EPAM Training WF Process"
        }
)
public class TrainingWorkflowProcess implements WorkflowProcess {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final String SERVICE_USER_NAME = "testSystemUser";
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @SlingObject
    private ResourceResolver resourceResolver;

    public void execute(WorkItem workItem, WorkflowSession workflowSession,
                        MetaDataMap metaDataMap) throws WorkflowException {

        final WorkflowData workflowData = workItem.getWorkflowData();
        Session session = workflowSession.getSession();

        String pageCurrent = workflowData.getPayload().toString();
        String destPath = null;
        Node nodeContent = null;
        String currentDirectory = null;

        Resource resource = getResourceResolver(workflowSession).getResource(pageCurrent);

        if(resource != null){
            nodeContent = resource.getChild("jcr:content").adaptTo(Node.class);
        }

//        check(workItem, workflowSession);

        try {
            currentDirectory = PathUtils.getAncestorPath(pageCurrent, 1);
//            nodePage = (Node) session.getItem(pageCurrent);
//            nodeContent = workflowSession.getSession().getItem(pageCurrent).getSession().getNode(pageCurrent+"/jcr:content");
            if (nodeContent.hasProperty("pathToMove")){
                destPath = nodeContent.getProperty("pathToMove").getString();
                String nodeAbsPath = destPath + "/" + PathUtils.getName(pageCurrent);
                if(!session.getWorkspace().getSession().nodeExists(nodeAbsPath) && !destPath.equals(currentDirectory)){
                    session.move(pageCurrent, nodeAbsPath);
                    session.save();
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {

        }
        log.info("executed");
    }

    // empty, invalid, or matches the current path
    boolean check(WorkItem workItem, WorkflowSession workflowSession){
        WorkflowData workflowData = workItem.getWorkflowData();
        String pageCurrent = workflowData.getPayload().toString();
        Session session = workflowSession.getSession();
        Node jcrNode = null;
        String currentDirectory = null;
        String nameNode = null;

        Item item = null;
        try {
            item = workflowSession.getSession().getItem(pageCurrent);
            currentDirectory = PathUtils.getAncestorPath(item.getPath(), 1);
            nameNode = item.getName();
            if (item.isNode()){
                jcrNode = item.getSession().getNode(pageCurrent+"/jcr:content");

                if (jcrNode.hasProperty("pathToMove")){
                    String destPath = jcrNode.getProperty("pathToMove").getString();

                    if (!destPath.isEmpty() && session.getRootNode().getPath().equals("/") && session.getRootNode().hasNode(destPath.substring(1))){

                        if(destPath.equals(currentDirectory)){
                            return false;
                        }
                        return true;
                    }
                    return false;
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final ResourceResolver getResourceResolver(WorkflowSession workflowSession){
        final Map<String, Object> authInfo = new HashMap<String, Object>();
        authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, workflowSession.getSession());
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getResourceResolver(authInfo);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        return resourceResolver;
    }
}