package com.epam.aem.core.listeners.versionpage;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface CreateVersionPageConfig {

    @AttributeDefinition(name = "NeedToClearHistoryAfterCreateVersion", type = AttributeType.BOOLEAN)
    boolean needToClearHistory() default false;

}
