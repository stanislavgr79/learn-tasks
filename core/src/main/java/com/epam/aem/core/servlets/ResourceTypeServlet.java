package com.epam.aem.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(service = Servlet.class,
        property = {
        Constants.SERVICE_DESCRIPTION + "=Simple HTML Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=demo",
                "sling.servlet.resourceTypes=" + "weretail/components/structure/page",
                "sling.servlet.extensions=" + "html" }

)
@Designate(ocd = ResourceTypeServlet.Configurations.class)
public class ResourceTypeServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @ObjectClassDefinition(name = "Resource Type Demo Servlet")
    public @interface Configurations {
        @AttributeDefinition(name = "Enter Path", description = "Page or node path")
        String getPath();
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.write("<html><body>");
        out.write("<h1>Welcome to keys and strokes technologoy blog</h1>");
        out.write("</html></body>");
        out.flush();
        out.close();
    }

    @Activate
    @Modified
    protected void Activate(Configurations config) {

    }

}
