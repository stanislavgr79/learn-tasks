package com.epam.aem.core.logic.querytest;

import com.day.cq.search.QueryBuilder;
import com.epam.aem.core.models.QueryTestModel;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = QuerySearchSelector.class)
public class QuerySearchSelector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String SERVICE_USER_NAME = "testSystemUser";
    private String sqlStatement;
    private final String QUERY_BUILDER = "queryBuilder";
    private final String QUERY_MANAGER = "queryManager";

    QuerySearchService querySearchService;
    private Session session;

    @Reference
    private QueryBuilder queryBuilder;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @SlingObject
    private ResourceResolver resourceResolver;

    public List<String> resultGet(QueryTestModel queryTestModel) {
        init(queryTestModel);
        return querySearchService.resultGet(this);
    }

    private void init(QueryTestModel queryTestModel){
        sqlStatement=
                "SELECT * FROM [nt:unstructured] AS base WHERE ISDESCENDANTNODE(base,'" + queryTestModel.getSearchPath() + "')" +
                        " and contains(base.*, '" + queryTestModel.getSearchText() + "')";
        buildSession();
        createSelectedQuerySearchService(queryTestModel.getTypeOfSearch());
        querySearchService.setQueryTestModel(queryTestModel);
    }

    private void createSelectedQuerySearchService(String typeOfSearch) {
        if (typeOfSearch.equals(QUERY_MANAGER)) querySearchService = new QuerySearchManagerImpl();
        if (typeOfSearch.equals(QUERY_BUILDER)) querySearchService = new QuerySearchBuilderImpl();
    }

    protected void buildSession() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            e.printStackTrace();
        }
        session = resourceResolver.adaptTo(Session.class);
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public Session getSession() {
        return session;
    }
}

//         //String jsonResponse2 = new ObjectMapper().writeValueAsString(resultMap);