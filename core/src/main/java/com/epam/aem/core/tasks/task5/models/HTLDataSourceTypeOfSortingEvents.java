package com.epam.aem.core.tasks.task5.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.epam.aem.core.tasks.task5.services.ServiceDataTypeOfSortingEventsModelAvailable;
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
        resourceType = "learn/components/tasks/eventviewertask5"
)
public class HTLDataSourceTypeOfSortingEvents {

    @SlingObject
    private ResourceResolver resourceResolver;
    @Inject
    private SlingHttpServletRequest request;
    @Inject
    private ServiceDataTypeOfSortingEventsModelAvailable serviceDataTypeOfSortingEventsModelAvailable;

    @ValueMapValue
    String[] typeOfSortingArray = {};

    @PostConstruct
    protected void init() {
        final Map<String, String> typeOfSorting = new LinkedHashMap<>();
        typeOfSortingArray = serviceDataTypeOfSortingEventsModelAvailable.getTypeOfSortingArray();
        for(String el: typeOfSortingArray){
            typeOfSorting.put(el,el);
        }
        DataSource ds = new SimpleDataSource(new TransformIterator(typeOfSorting.keySet().iterator(), o -> {
            String typeOfSort = (String) o;
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("value", typeOfSorting.get(typeOfSort).toLowerCase());
            vm.put("text", typeOfSort);

            return new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", vm);
        }));
        request.setAttribute(DataSource.class.getName(), ds);
    }

    public String[] getTypeOfSortingArray() {
        return typeOfSortingArray;
    }
}
