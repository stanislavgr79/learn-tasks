package com.epam.aem.core.tasks.task4.logic;

import com.day.cq.search.QueryBuilder;
import com.epam.aem.core.tasks.task4.models.SearchModelBean;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = QuickSearchSelector.class)
public class QuickSearchSelector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "systemuser";

    private SearchService searchService;
    private Session session;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @SlingObject
    private ResourceResolver resourceResolver;
    @Reference
    private QueryBuilder queryBuilder;

    public Map<String, String> resultGet(SearchModelBean searchModelBean) {
        init(searchModelBean);
        return searchService.resultGet(this);
    }

    private void init(SearchModelBean searchModelBean){
        buildSession();
        createSelectedQuerySearchService(searchModelBean.getTypeOfSearch());
        searchService.setSearchModelBean(searchModelBean);
    }

    private void createSelectedQuerySearchService(String typeOfSearch) {
        if (typeOfSearch.equals("XPath")) searchService = new SearchByManagerXPathImpl();
        if (typeOfSearch.equals("SQL2")) searchService = new SearchByManagerSQL2Impl();
        if (typeOfSearch.equals("Predicates")) searchService = new SearchByBuilderPredicatesImpl();
    }

    protected void buildSession() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            logger.error(String.format("buildSession: %s", e.getMessage()));
        }
        session = resourceResolver.adaptTo(Session.class);
    }
    
    public Session getSession() {
        return session;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}

