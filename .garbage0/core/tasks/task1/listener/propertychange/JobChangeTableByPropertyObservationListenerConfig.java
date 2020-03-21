package com.epam.aem.core.tasks.task1.listener.propertychange;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobChangeTableByPropertyObservationListenerConfig {

    @AttributeDefinition(name = "ChangeTableByPropertyObservationListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;
}
