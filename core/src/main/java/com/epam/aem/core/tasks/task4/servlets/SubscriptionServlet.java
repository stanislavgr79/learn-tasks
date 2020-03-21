package com.epam.aem.core.tasks.task4.servlets;

import com.epam.aem.core.tasks.task4.logic.QuickSearchSelector;
import com.epam.aem.core.tasks.task4.models.SearchModelBean;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Simple HTML Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=data",
                "sling.servlet.resourceTypes=" + "learn/components/tasks/searchtask4",
                "sling.servlet.extensions=" + "json" }

)
public class SubscriptionServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient QuickSearchSelector quickSearchSelector;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        final Resource resource = request.getResource();
        String searchWord = request.getParameter("search1");
        String path = resource.getPath().substring(0, resource.getPath().indexOf("/jcr:content"));
        String typeOfSearch = resource.getValueMap().get("typeOfSearch").toString();

        SearchModelBean searchModelBean = new SearchModelBean(searchWord, path, typeOfSearch);
        Map<String, String> resultMap = new HashMap<>();

        if(searchWord.length()>2){
            resultMap = quickSearchSelector.resultGet(searchModelBean);
        }

        String jsonResponse = new Gson().toJson(resultMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
