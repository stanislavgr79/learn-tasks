package com.epam.aem.core.tasks.task5.servlets;

import com.epam.aem.core.tasks.task5.models.EventComponent;
import com.epam.aem.core.tasks.task5.services.EventService;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Eventviewertask5 Servlet EventAll",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=all",
                "sling.servlet.resourceTypes=" + "learn/components/tasks/eventviewertask5",
                "sling.servlet.extensions=" + "json" }

)
public class EventAllServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient EventService eventService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        final Resource resource = request.getResource();
        int paginationElements = Integer.parseInt(resource.getValueMap().get("paginationElements").toString());
        String pathRequest = resource.getPath();

        String jsonResponse = "";
        if(paginationElements>=0){
            List<EventComponent> eventComponents = null;
            try {
                eventComponents = eventService.getAllEvents(pathRequest);
            } catch (RepositoryException e) {
                final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
                logger.error(String.format("initialization: %s", e.getMessage()));
            }
            jsonResponse = new Gson().toJson(eventComponents);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
