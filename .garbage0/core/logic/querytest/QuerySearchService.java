package com.epam.aem.core.logic.querytest;

import com.epam.aem.core.models.QueryTestModel;

import javax.jcr.Session;
import java.util.List;

public interface QuerySearchService {

    List<String> resultGet(QuerySearchSelector querySearchSelector);
    void setQueryTestModel(QueryTestModel queryTestModel);
}
