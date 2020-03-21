package com.epam.aem.core.filters;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface FilterImageConfig {

    @AttributeDefinition(name = "rotate", type = AttributeType.BOOLEAN)
    boolean rotate() default false;

    @AttributeDefinition(name = "grayscale", type = AttributeType.BOOLEAN)
    boolean grayscale() default false;

}
