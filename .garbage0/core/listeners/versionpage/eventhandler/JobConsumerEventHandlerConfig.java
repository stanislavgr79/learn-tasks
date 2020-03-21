package com.epam.aem.core.listeners.versionpage.eventhandler;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface JobConsumerEventHandlerConfig {

    @AttributeDefinition(name = "CustomEventHandler", type = AttributeType.BOOLEAN)
    boolean activateTopic() default false;

}
