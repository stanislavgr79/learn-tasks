package com.epam.aem.core.tasks.task5.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(immediate = true, service = ServiceDataTypeOfSortingEventsModelAvailable.class)
@ServiceDescription("Task5 Events serviceSorting available - Task5")
@Designate(ocd = ServiceDataTypeOfSortingEventsModelAvailable.ServiceDataSortingEventsConfig.class)
public class ServiceDataTypeOfSortingEventsModelAvailable {

    private String [] typeOfSortingArray = {};

    @ObjectClassDefinition
    public @interface ServiceDataSortingEventsConfig {
        @AttributeDefinition(name = "sortOrderBy column", type = AttributeType.STRING)
        String[] typeOfSortingArray() default {"Any"};
    }

    @Activate
    @Modified
    public void activate(ServiceDataSortingEventsConfig config) {
        this.typeOfSortingArray = config.typeOfSortingArray();
    }

    public String[] getTypeOfSortingArray() {
        return typeOfSortingArray;
    }
}
