package com.epam.aem.core.tasks.task5.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EventViewer {

    @ValueMapValue
    private String columnNameForSorting;
    @ValueMapValue
    private int paginationElements;

    public String getColumnNameForSorting() {
        return columnNameForSorting;
    }

    public int getPaginationElements() {
        return paginationElements;
    }
}
