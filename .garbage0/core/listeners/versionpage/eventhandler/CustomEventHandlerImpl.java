package com.epam.aem.core.listeners.versionpage.eventhandler;

import com.day.cq.wcm.api.*;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//@Component(service = EventHandler.class,
//        immediate = true,
//        property = {
//                Constants.SERVICE_DESCRIPTION + "=Demo to listen on changes in the resource tree",
//                EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC
////                ,EventConstants.EVENT_FILTER + "=" + "(path=/content/learn/us/*)"
////                ,EventConstants.EVENT_FILTER + "=glob:/content/learn/us/*/jcr:content/**"
//        })

@Component(service = {EventHandler.class},
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Demo to listen on changes in the resource tree",
                EventConstants.EVENT_TOPIC + "=" + "org/apache/sling/api/resource/Resource/*",
//                EventConstants.EVENT_TOPIC + "=" + "(&"+ "(org/apache/sling/api/resource/Resource/ADDED) " + "(org/apache/sling/api/resource/Resource/CHANGED)" + ")",
                EventConstants.EVENT_FILTER + "=" + "(&"+ "(path=/content/learn/us/*/jcr:content)" + "(resourceType=*/page)" + ")"
//                " (|(" + SlingConstants
//                .PROPERTY_CHANGED_ATTRIBUTES + "=*jcr:title) "
//                + "(" + ResourceChangeListener.CHANGES + "=*jcr:title)))"
        })

@Designate(ocd = CustomEventHandlerImplConfig.class)
public class CustomEventHandlerImpl implements EventHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private boolean activateType1;
    private boolean activateType2;
    private int value;

    @Activate
    @Modified
    public void activate(CustomEventHandlerImplConfig  customEventHandlerImplConfig) {
        this.activateType1 = customEventHandlerImplConfig.activateType1();
        this.activateType2 = customEventHandlerImplConfig.activateType2();
    }

    @Activate
    protected void activate() {
    }

    @Reference
    JobManager jobManager;

    @Override
    public void handleEvent(Event event) {
        Map<String, Object> properties = new HashMap<>();
        if (activateType1){
            value = 1;
        } else {
            if (activateType2) {
                value = 2;
            }
        }
        switch (value){
            case 1: {
                properties.put("pageEventByEventHandler", PageEvent.fromEvent(event));
                jobManager.createJob("aem/custom/pageEventByEventHandler");
                jobManager.addJob("aem/custom/pageEventByEventHandler", properties);
                break;
            }
            case 2: {
                properties.put("pageEventByEventHandlerPath", event.getProperty("path"));
                jobManager.createJob("aem/custom/pageEventByEventHandler");
                jobManager.addJob("aem/custom/pageEventByEventHandler", properties);
                break;
            }
            default: break;
        }
    }
}