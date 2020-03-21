package com.epam.aem.core.servlets;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.Workflow;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component(immediate = true,
            service = SlingSafeMethodsServlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=WorkflowServlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "sling.servlet.default",
                "sling.servlet.extensions=" + "abc"
        }
)
public class WorkflowServlet extends SlingSafeMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private static final String MODEL_PATH = "/var/workflow/models/workflow-training";

    @Reference
    private WorkflowService workflowService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter printWriter = response.getWriter();

        Workflow workflow;

        Session session = request.getResourceResolver().adaptTo(Session.class);

        WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
        WorkflowModel workflowModel = null;
        try {
            workflowModel = workflowSession.getModel(MODEL_PATH);
            String resourcePath = request.getRequestPathInfo().getResourcePath();
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", resourcePath);
            workflow = workflowSession.startWorkflow(workflowModel, workflowData);
        } catch (WorkflowException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        printWriter.write("<p>" + "Workflow launched: " + workflow.getId() + "</p>");
        printWriter.flush();
        printWriter.close();
    }
}
