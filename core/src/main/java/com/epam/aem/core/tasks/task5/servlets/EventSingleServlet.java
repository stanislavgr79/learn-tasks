package com.epam.aem.core.tasks.task5.servlets;

import com.epam.aem.core.tasks.task5.models.EventComponent;
import com.epam.aem.core.tasks.task5.services.EventService;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Eventviewertask5 Servlet EventSingle",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=single",
                "sling.servlet.resourceTypes=" + "learn/components/tasks/eventviewertask5",
                "sling.servlet.extensions=" + "json" }

)
public class EventSingleServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient EventService eventService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        String searchByName = request.getParameter("searchEvent");

        String jsonResponse = "";
        EventComponent event = null;
        if(!searchByName.equals("")){
            try {
                event = eventService.getEvent(searchByName);
            } catch (RepositoryException e) {
                final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
                logger.error(String.format("initialization: %s", e.getMessage()));
            }
                jsonResponse = new Gson().toJson(event);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
