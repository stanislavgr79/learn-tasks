package com.epam.aem.core.models;

import com.epam.aem.core.logic.querytest.QuerySearchSelector;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class QueryTestModel {

    @Inject
    private QuerySearchSelector querySearchSelector;

    @Inject
    @Named(value = "searchText")
    private String searchText;
    @Inject
    @Named(value = "searchPath")
    private String searchPath;
    @Inject
    @Named(value = "typeOfSearch")
    private String typeOfSearch;


    private String queryStr = "";
    private List<String> resultList = new ArrayList<>();


    public String getSearchText() {
        return searchText;
    }
    public String getSearchPath() {
        return searchPath;
    }
    public String getTypeOfSearch() {
        return typeOfSearch;
    }

    public String getQueryStr() {
        return queryStr;
    }
    public List<String> getResultList() {
        return resultList;
    }

    @PostConstruct
    protected void init() {
        littleTest();
        if (searchText == null || searchPath == null) {
            return;
        }
        resultList = querySearchSelector.resultGet(this);
    }

    private void littleTest() {
        queryStr = "SELECT * FROM [nt:unstructured] WHERE ISDESCENDANTNODE('" + searchPath + "') " +
                "AND CONTAINS('" + searchText + "')";
    }

}
