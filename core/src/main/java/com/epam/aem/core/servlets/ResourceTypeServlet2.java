//package com.epam.aem.core.servlets;
//
//import com.google.gson.Gson;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ValueMap;
//import org.apache.sling.api.servlets.HttpConstants;
//import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//import org.osgi.framework.Constants;
//import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Modified;
//import org.osgi.service.metatype.annotations.AttributeDefinition;
//import org.osgi.service.metatype.annotations.Designate;
//import org.osgi.service.metatype.annotations.ObjectClassDefinition;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component(service = Servlet.class,
//        property = {
//        Constants.SERVICE_DESCRIPTION + "=Simple HTML Demo Servlet",
//                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
//                "sling.servlet.selectors=data",
//                "sling.servlet.resourceTypes=" + "learn/components/tasks/searchtask4",
//                "sling.servlet.extensions=" + "json" }
//
//)
//public class ResourceTypeServlet2 extends SlingSafeMethodsServlet {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
//
//    @Override
//    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
//        final Resource resource = request.getResource();
//        String searchWord = request.getParameter("param1");
//        String pathPage = request.getParameter("param2");
//        ValueMap valueMap = resource.adaptTo(ValueMap.class);
//        ValueMap valueMap1 = resource.getValueMap();
//
//        logger.info("====================================================SubscriptionServlet::::::::::SubscriptionConfiguration=====================================");
//        List<String> list = new ArrayList<>();
//        list.add("qwe123");
//        list.add("asd123");
//        list.add("zxc123");
//
//        String jsonResponse = new Gson().toJson(list);
////        String jsonResponse = new ObjectMapper().writeValueAsString(list);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonResponse);
//
//    }
//}
