package com.epam.aem.core.logic.querytest;

import com.day.cq.search.Predicate;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.epam.aem.core.models.QueryTestModel;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = QuerySearchService.class)
public class QuerySearchBuilderImpl implements QuerySearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private QueryTestModel queryTestModel;

    @Override
    public void setQueryTestModel(QueryTestModel queryTestModel) {
        this.queryTestModel = queryTestModel;
    }

    @Override
    public List<String> resultGet(QuerySearchSelector querySearchSelector) {
        List<String> resultOfQuerySearch = new ArrayList<>();

        Session session = querySearchSelector.getSession();
        QueryBuilder queryBuilder = querySearchSelector.getQueryBuilder();

        Map<String, String> predicateMap = new HashMap<String, String>() {{
            put("path", queryTestModel.getSearchPath());
            put("fulltext", queryTestModel.getSearchText());
            put("type", "nt:unstructured");
        }};

        PredicateGroup predicateGroup = new PredicateGroup();
        predicateGroup.add(new Predicate("path", "path").set("path", queryTestModel.getSearchPath()));
        predicateGroup.add(new Predicate("type", "type").set("type", "nt:unstructured"));
        predicateGroup.add(new Predicate("fulltext", "fulltext").set("fulltext", queryTestModel.getSearchText()));

        com.day.cq.search.Query query1 = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
//        com.day.cq.search.Query query2 = queryBuilder.createQuery(predicateGroup, session);

//        query1.setStart(0);
//        query1.setHitsPerPage(10);

        SearchResult result = query1.getResult();
        List<Hit> list = result.getHits();

        for (Hit hit : list) {
            String el = null;
            try {
                el = hit.getPath();
            } catch (RepositoryException e) {
                e.printStackTrace();
            } finally {
                session.logout();
            }
            resultOfQuerySearch.add(el);
        }
        return resultOfQuerySearch;
    }
}
