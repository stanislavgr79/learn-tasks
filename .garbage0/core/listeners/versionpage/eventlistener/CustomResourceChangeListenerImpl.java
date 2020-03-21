package com.epam.aem.core.listeners.versionpage.eventlistener;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        service = ResourceChangeListener.class,
        property = {
        ResourceChangeListener.CHANGES + "=CHANGED",
        ResourceChangeListener.CHANGES + "=ADDED",
        ResourceChangeListener.CHANGES + "=REMOVED",
        ResourceChangeListener.PATHS + "=glob:/content/learn/us/*/jcr:content/**"
    }
)
public class CustomResourceChangeListenerImpl implements ResourceChangeListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Reference
    JobManager jobManager;

    public void onChange(List<ResourceChange> resourceChangeList) {
        Map<String, Object> properties = new HashMap<>();
        if (resourceChangeList.size()>1){
            properties.put("pageEventByResourceChangeListener", resourceChangeList.listIterator().next().getPath());
            jobManager.addJob("aem/custom/pageEventByResourceChangeListener", properties);
        }
    }
}
