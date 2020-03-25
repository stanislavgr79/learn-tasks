package com.epam.aem.core.tasks.task5.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(immediate = true, service = ServiceDataEventsTypeOfTopicModelAvailable.class)
@ServiceDescription("Task5 Events service Type of topic available - Task5")
@Designate(ocd = ServiceDataEventsTypeOfTopicModelAvailable.ServiceDataEventsTypeOfTopicModelAvailableConfig.class)
public class ServiceDataEventsTypeOfTopicModelAvailable {

    private String [] typeOfTopicArray = {};

    @ObjectClassDefinition
    public @interface ServiceDataEventsTypeOfTopicModelAvailableConfig {
        @AttributeDefinition(name = "Type of topic", type = AttributeType.STRING)
        String[] typeOfTopicArray() default {"Any"};
    }

    @Activate
    @Modified
    public void activate(ServiceDataEventsTypeOfTopicModelAvailableConfig config) {
        this.typeOfTopicArray = config.typeOfTopicArray();
    }

    public String[] getTypeOfTopicArray() {
        return typeOfTopicArray;
    }
}
