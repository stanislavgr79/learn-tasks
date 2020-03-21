package com.epam.aem.core.tasks.task4.logic;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.epam.aem.core.tasks.task4.models.SearchModelBean;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(immediate = true, service = SearchService.class)
public class SearchByBuilderPredicatesImpl implements SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SearchModelBean searchModelBean;

    @Override
    public void setSearchModelBean(SearchModelBean searchModelBean) {
        this.searchModelBean = searchModelBean;
    }

    @Override
    public Map<String, String> resultGet(QuickSearchSelector quickSearchSelector) {
        Map<String, String> resultOfQuerySearch = new HashMap<>();
        QueryBuilder queryBuilder = quickSearchSelector.getQueryBuilder();
        Session session = quickSearchSelector.getSession();

        Map<String, String> predicateMap = Map.of(
                "path", searchModelBean.getSearchPath(),
                "fulltext", searchModelBean.getSearchText(),
                "type", "nt:base");

        Query query1 = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);

        query1.setStart(0);
        query1.setHitsPerPage(10);

        SearchResult result = query1.getResult();
        List<Hit> queries = result.getHits();

        for (Hit hit : queries) {
            String element = null;
            try {
                element = hit.getPath();
                Node node = session.getNode(element);
                while (!node.getProperty("jcr:primaryType").getValue().getString().equals("cq:Page")){
                    node = node.getParent();
                }
                String key = node.getNode("jcr:content").getProperty("jcr:title").getString();
                String value = node.getPath();
                resultOfQuerySearch.put(key, value);
            } catch (RepositoryException e) {
                logger.error(String.format("hit.getPath: %s", e.getMessage()));
            }
        }
        session.logout();
        return resultOfQuerySearch;
    }
}
