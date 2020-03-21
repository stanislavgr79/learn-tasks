/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.epam.aem.core.filters;

import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.cq.wcm.foundation.Image;
import com.day.image.Layer;
import com.epam.aem.core.logic.image.LogicImage;
import com.epam.aem.core.logic.image.LogicImageGrayscaleImpl;
import com.epam.aem.core.logic.image.LogicImageRotateImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.*;
import java.io.IOException;

/**
 * Simple servlet filter component that logs incoming requests.
 */
@Component(service = Filter.class,
           property = {
                   EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
                   EngineConstants.SLING_FILTER_PATTERN + "=" + "/content/we-retail/.*",
                   EngineConstants.SLING_FILTER_EXTENSIONS + "=" + "jpg",
                   EngineConstants.SLING_FILTER_EXTENSIONS + "=" + "png",
                   EngineConstants.SLING_FILTER_EXTENSIONS + "=" + "gif",
                   EngineConstants.SLING_FILTER_EXTENSIONS + "=" + "jpeg",
           })
@ServiceDescription("Demo to filter incoming requests")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
@Designate(ocd = FilterImageConfig.class)
public class FilterImage extends AbstractImageServlet implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private boolean rotate;
    private boolean grayscale;

    @Activate
    @Modified
    public void activate(FilterImageConfig filterImageConfig) {
        this.rotate = filterImageConfig.rotate();
        this.grayscale = filterImageConfig.grayscale();
    }

    @Reference
    private LogicImage logicImage;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {

        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

        logger.debug("request for {}, with selector {}", slingRequest
                .getRequestPathInfo().getResourcePath(), slingRequest
                .getRequestPathInfo().getSelectorString());

        String extension = slingRequest.getRequestPathInfo().getExtension();
        String imageType = getImageType(extension);

        response.setContentType(imageType);
        ImageContext context = new ImageContext(slingRequest, imageType);

        try {
            Layer layer = null;
            layer = createLayer(context);
            layer = changeImageByFilterConfigEnvironment(layer);
            writeLayer(slingRequest, slingResponse, context, layer);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected Layer createLayer(ImageContext imageContext) throws RepositoryException, IOException {
        Image image = new Image(imageContext.resource);
        return image.getLayer(false, false, true);
    }


    private Layer changeImageByFilterConfigEnvironment(Layer layer){
        if(rotate) {
            logicImage = new LogicImageRotateImpl();
            layer = logicImage.changeImageByConcreteFilterConfigEnvironment(layer);
        }
        if(grayscale){
            logicImage = new LogicImageGrayscaleImpl();
            logicImage.changeImageByConcreteFilterConfigEnvironment(layer);
        }
        return layer;
    }

    @Override
    protected void writeLayer(SlingHttpServletRequest request, SlingHttpServletResponse response, ImageContext context, Layer layer) throws IOException, RepositoryException {
        super.writeLayer(request, response, context, layer);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    protected String getImageType(String ext) {
        if ("png".equals(ext))
            return "image/png";
        if ("gif".equals(ext))
            return "image/gif";
        if (("jpg".equals(ext)) || ("jpeg".equals(ext))) {
            return "image/jpg";
        }
        return null;
    }
}