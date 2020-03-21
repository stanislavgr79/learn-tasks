package com.epam.aem.core.models.observation;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface TableListenerConfig {

    @AttributeDefinition(name = "TableListener", type = AttributeType.BOOLEAN)
    boolean activateTopic() default true;
}
