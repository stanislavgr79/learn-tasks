//
//package com.epam.aem.core.servlets;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gson.Gson;
//import org.apache.felix.scr.annotations.sling.SlingServlet;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.servlets.SlingAllMethodsServlet;
//import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//
//import javax.servlet.ServletException;
//import org.osgi.service.component.propertytypes.ServiceDescription;
//
///**
// * Servlet that writes some sample content into the response. It is mounted for
// * all resources of a specific Sling resource type. The
// * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
// * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
// */
//@SlingServlet(methods = {"GET"},
//        metatype = true,
//        resourceTypes = {"learn/components/tasks/searchtask4"},
//        selectors = {"html"},
//        extensions = {"json"}
//)
//@ServiceDescription("Simple Demo Servlet")
//public class SimpleServlet4 extends SlingSafeMethodsServlet {
//
//    private static final long serialVersionUID = 1L;
//
//    @Override
//    protected void doGet(final SlingHttpServletRequest request,
//            final SlingHttpServletResponse response) throws ServletException, IOException {
//        final Resource resource = request.getResource();
//        System.out.println("1111111");
//
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
//    }
//}
