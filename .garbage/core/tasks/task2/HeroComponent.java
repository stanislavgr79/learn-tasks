package com.epam.aem.core.tasks.task2;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroComponent {

    @ValueMapValue
    private String plaintext;
    @ValueMapValue
    private String richtext;
    @ValueMapValue
    private String label;
    @ValueMapValue
    private String linkTo;
    @ValueMapValue
    private String typeOfOpen;

    public String getPlaintext() {
        return plaintext;
    }

    public String getRichtext() {
        return richtext;
    }

    public String getLabel() {
        return label;
    }

    public String getLinkTo() {
        return linkTo;
    }

    public String getTypeOfOpen() {
        return typeOfOpen;
    }
}
