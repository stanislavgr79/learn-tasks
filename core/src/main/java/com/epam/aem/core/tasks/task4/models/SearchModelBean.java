package com.epam.aem.core.tasks.task4.models;

public class SearchModelBean {

    private String searchText;
    private String typeOfSearch;
    private String searchPath;

    public SearchModelBean(String searchText, String searchPath, String typeOfSearch) {
        this.searchText = searchText;
        this.searchPath = searchPath;
        this.typeOfSearch = typeOfSearch;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public void setSearchPath(String searchPath) {
        this.searchPath = searchPath;
    }

    public String getTypeOfSearch() {
        return typeOfSearch;
    }

    public void setTypeOfSearch(String typeOfSearch) {
        this.typeOfSearch = typeOfSearch;
    }
}
