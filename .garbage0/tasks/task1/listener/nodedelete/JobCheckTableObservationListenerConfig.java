package com.epam.aem.core.tasks.task1.listener.nodedelete;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobCheckTableObservationListenerConfig {

    @AttributeDefinition(name = "CheckTableObservationListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;
}
