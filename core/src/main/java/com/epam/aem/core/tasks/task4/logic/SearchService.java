package com.epam.aem.core.tasks.task4.logic;

import com.epam.aem.core.tasks.task4.models.SearchModelBean;

import java.util.Map;

public interface SearchService {

    Map<String, String> resultGet(QuickSearchSelector quickSearchSelector);
    void setSearchModelBean(SearchModelBean searchModelBean);
}
