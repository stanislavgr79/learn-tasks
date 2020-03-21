package com.epam.aem.core.listeners.versionpage.eventlistener;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobConsumerResourceChangeListenerConfig {

    @AttributeDefinition(name = "ResourceChangeListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;
}
