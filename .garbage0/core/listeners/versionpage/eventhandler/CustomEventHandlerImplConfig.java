package com.epam.aem.core.listeners.versionpage.eventhandler;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface CustomEventHandlerImplConfig {

    @AttributeDefinition(name = "CustomEventHandlerPage", type = AttributeType.BOOLEAN)
    boolean activateType1() default false;

    @AttributeDefinition(name = "CustomEventHandlerPath", type = AttributeType.BOOLEAN)
    boolean activateType2() default false;

}
