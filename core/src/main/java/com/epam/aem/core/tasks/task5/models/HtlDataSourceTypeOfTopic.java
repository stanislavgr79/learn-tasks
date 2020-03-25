package com.epam.aem.core.tasks.task5.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.epam.aem.core.tasks.task5.services.ServiceDataEventsTypeOfTopicModelAvailable;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "learn/components/tasks/eventcomponenttask5"
)
public class HtlDataSourceTypeOfTopic {

    @SlingObject
    private ResourceResolver resourceResolver;
    @Inject
    private SlingHttpServletRequest request;
    @Inject
    private ServiceDataEventsTypeOfTopicModelAvailable serviceDataEventsTypeOfTopicModelAvailable;

    @ValueMapValue
    String[] arrayTypeOfTopics = {};

    @PostConstruct
    protected void init() {
        final Map<String, String> topicTypes = new LinkedHashMap<>();
        arrayTypeOfTopics = serviceDataEventsTypeOfTopicModelAvailable.getTypeOfTopicArray();
        for(String el: arrayTypeOfTopics){
            topicTypes.put(el,el);
        }
        DataSource ds = new SimpleDataSource(new TransformIterator(topicTypes.keySet().iterator(), o -> {
            String topicName = (String) o;
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("value", topicTypes.get(topicName));
            vm.put("text", topicName);

            return new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", vm);
        }));
        request.setAttribute(DataSource.class.getName(), ds);
    }

    public String[] getArrayTypeOfTopics() {
        return arrayTypeOfTopics;
    }
}
