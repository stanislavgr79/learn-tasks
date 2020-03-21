package com.epam.aem.core.tasks.task4.logic;

import com.epam.aem.core.tasks.task4.models.SearchModelBean;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import java.util.*;

@Component(immediate = true, service = SearchService.class)
public class SearchByManagerSQL2Impl implements SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private String sqlStatement;

    @Override
    public void setSearchModelBean(SearchModelBean searchModelBean) {
        sqlStatement =
                "SELECT * from [nt:base] as base" +
                        " WHERE ISDESCENDANTNODE(base,'" + searchModelBean.getSearchPath() + "/')" +
                        " and contains(base.*, '" + searchModelBean.getSearchText() + "')";
    }

    @Override
    public Map<String, String> resultGet(QuickSearchSelector quickSearchSelector) {
        Session session = quickSearchSelector.getSession();
        Map<String, String> map = new HashMap<>();

        QueryManager queryManager = null;
        try {
            queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(sqlStatement, "JCR-SQL2");
            javax.jcr.query.QueryResult result = query.execute();
            NodeIterator nodeItem = result.getNodes();
            while (nodeItem.hasNext()) {
                javax.jcr.Node node = nodeItem.nextNode();
                while (!node.getProperty("jcr:primaryType").getValue().getString().equals("cq:Page")){
                    node = node.getParent();
                }
                String key = node.getNode("jcr:content").getProperty("jcr:title").getString();
                String value = node.getPath();
                map.put(key, value);
            }
        } catch (RepositoryException e) {
            logger.error(String.format("resultGet: %s", e.getMessage()));
        } finally {
            session.logout();
        }
        return map;
    }
}
