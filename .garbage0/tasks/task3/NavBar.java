package com.epam.aem.core.tasks.task3;

import java.util.*;
import java.util.Iterator;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

import com.adobe.cq.sightly.WCMUsePojo;

public class NavBar extends WCMUsePojo {

    private List<Page> items = new ArrayList<>();
    private Page rootPage;
    private Locale locale;

    @Override
    public void activate(){
        rootPage = getCurrentPage().getAbsoluteParent(1);

        if (rootPage == null) {
            rootPage = getCurrentPage();
        }

        Iterator<Page> childPages = getCurrentPage().listChildren(new PageFilter(getRequest()));
        while (childPages.hasNext()) {
            items.add(childPages.next());
        }
    }

    // Returns the navigation items
    public List<Page> getItems() {
        return items;
    }

    // Returns the navigation root
    public Page getRootPage() {
        return rootPage;
    }

    // Returns the current Language
    public String getLocale() {
        return locale.getLanguage();
    }
}
