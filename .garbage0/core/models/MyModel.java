package com.epam.aem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class)
public class MyModel {

    @Inject
    @Optional
    @Named(value = "name")
    private String name;

    @Inject
    @Optional
    @Named(value = "typeOfSearch")
    private String query;


    public String getQuery() {
        return query;
    }

    public String getName() {
        return name;
    }
}
