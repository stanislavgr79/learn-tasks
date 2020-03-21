package com.epam.aem.core.listeners.versionpage.observation;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobConsumerObservationListenerConfig {

    @AttributeDefinition(name = "ObservationListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;
}
