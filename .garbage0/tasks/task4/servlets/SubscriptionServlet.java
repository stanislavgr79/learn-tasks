package com.epam.aem.core.tasks.task4.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true,
        service = Servlet.class,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=" + "/bin/myservlet",
                "sling.servlet.extensions=" + "json",
                "sling.servlet.selectors=" + "data"
        }
)
public class SubscriptionServlet extends SlingSafeMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        final Resource resource = request.getResource();
        String searchWord = request.getParameter("search1");
        ValueMap valueMap = resource.adaptTo(ValueMap.class);
        ValueMap valueMap1 = resource.getValueMap();

        logger.info("====================================================SubscriptionServlet::::::::::SubscriptionConfiguration=====================================");
        List<String> list = new ArrayList<>();
        list.add("qwe123");
        list.add("asd123");
        list.add("zxc123");

        String jsonResponse = new Gson().toJson(list);
//        String jsonResponse = new ObjectMapper().writeValueAsString(list);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

    }
}
