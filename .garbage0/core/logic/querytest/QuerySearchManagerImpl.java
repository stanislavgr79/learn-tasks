package com.epam.aem.core.logic.querytest;

import com.epam.aem.core.models.QueryTestModel;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = QuerySearchService.class)
public class QuerySearchManagerImpl implements QuerySearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private String sqlStatement;

    private QueryTestModel queryTestModel;

    @Override
    public void setQueryTestModel(QueryTestModel queryTestModel) {
        this.queryTestModel = queryTestModel;
        sqlStatement =
                "SELECT * FROM [nt:unstructured] AS base WHERE ISDESCENDANTNODE(base,'" + queryTestModel.getSearchPath() + "')" +
                        " and contains(base.*, '" + queryTestModel.getSearchText() + "')";
    }

    @Override
    public List<String> resultGet(QuerySearchSelector querySearchSelector) {
        Session session = querySearchSelector.getSession();
        List<String> resultOfQuerySearch = new ArrayList<>();

        //Obtain the query manager for the session ...
        QueryManager queryManager = null;
        try {
            queryManager = session.getWorkspace().getQueryManager();

            Query query = queryManager.createQuery(sqlStatement, "JCR-SQL2");
            //Execute the query and get the results ...
            javax.jcr.query.QueryResult result = query.execute();
            //Iterate over the nodes in the results ...
            javax.jcr.NodeIterator nodeItem = result.getNodes();
            while (nodeItem.hasNext()) {
                javax.jcr.Node node = nodeItem.nextNode();
                resultOfQuerySearch.add(node.getPath());
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
        return resultOfQuerySearch;
    }
}
