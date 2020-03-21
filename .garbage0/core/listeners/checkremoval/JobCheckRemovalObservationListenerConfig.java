package com.epam.aem.core.listeners.checkremoval;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobCheckRemovalObservationListenerConfig {

    @AttributeDefinition(name = "CheckRemovalObservationListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;
}
