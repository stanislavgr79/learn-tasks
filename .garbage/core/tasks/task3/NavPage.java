package com.epam.aem.core.tasks.task3;

import com.day.cq.wcm.api.Page;

import java.util.List;

public class NavPage {

    private String title;
    private Page current;
    private String findTitle;
    private boolean lastPage;

    private List<Page> childPage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Page> getChildPage() {
        return childPage;
    }

    public void setChildPage(List<Page> childPage) {
        this.childPage = childPage;
    }

    public Page getCurrent() {
        return current;
    }

    public void setCurrent(Page current) {
        this.current = current;
    }

    public String getFindTitle() {
        return findTitle;
    }

    public void setFindTitle(String findTitle) {
        this.findTitle = findTitle;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
