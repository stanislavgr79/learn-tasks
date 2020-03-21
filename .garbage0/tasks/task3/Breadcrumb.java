package com.epam.aem.core.tasks.task3;

import java.util.*;
import java.util.Iterator;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

import static java.util.stream.Collectors.toMap;

public class Breadcrumb extends WCMUsePojo {
    private Logger LOG = LoggerFactory.getLogger(Breadcrumb.class);

    private Page rootPage;
    private Page mainPage;
    private Page selectedPage;

    //withOutCheckTitle
    private LinkedList<Page> listOfPage;
    //withCheckAvailableTitle
    private Map<Integer, NavPage> pageWithTitle;
    //withChildAllParent
    private Map<String, NavPage> mapOfPages;

    @Override
    public void activate() throws Exception {
        mainPage = selectedPage = getCurrentPage();
        rootPage = mainPage.getAbsoluteParent(1);
        if (rootPage == null) rootPage = getCurrentPage();

        actionGetOnlyParentPageWithAvailableTitle();
//        actionGetOnlyParentPage();
//        actionWithGetChildPage();
    }

    private void actionGetOnlyParentPageWithAvailableTitle() {
        int depthRootPage = rootPage.getDepth();
        int depthCurrentPage = selectedPage.getDepth();
        pageWithTitle = new LinkedHashMap<>();

        for (int i = depthCurrentPage; i > depthRootPage; i--) {
            NavPage navPage = new NavPage();
            if (mainPage.getDepth() == selectedPage.getDepth()) navPage.setLastPage(true);
            if (!checkPageIsHiddenInNav(selectedPage)) {
                String avTitle = getAvailableTitle(selectedPage);
                navPage.setFindTitle(avTitle);
                navPage.setCurrent(selectedPage);
                pageWithTitle.put(selectedPage.getDepth(), navPage);
            }
            selectedPage = selectedPage.getParent();
        }
        pageWithTitle = returnSortedByKeyIntReversedMap(pageWithTitle);
    }

    private void actionGetOnlyParentPage() {
        int depthRootPage = rootPage.getDepth();
        int depthCurrentPage = selectedPage.getDepth();
        listOfPage = new LinkedList<>();

        for (int i = depthCurrentPage; i > depthRootPage + 1; i--) {
            selectedPage = selectedPage.getParent();
            if (!checkPageIsHiddenInNav(selectedPage)) {
                listOfPage.addLast(selectedPage);
            }
            selectedPage = selectedPage.getParent();
        }
    }
    private void actionWithGetChildPage() {
        int depthRootPage = rootPage.getDepth();
        int depthCurrentPage = selectedPage.getDepth();
        for (int i = depthCurrentPage; i > depthRootPage + 1; i--) {
            NavPage navPage = new NavPage();
            selectedPage = selectedPage.getParent();

            if (!checkPageIsHiddenInNav(selectedPage)) {
                LinkedList<Page> childOfPage = new LinkedList<>();
                Iterator<Page> elIterator = selectedPage.listChildren(new PageFilter(getRequest()));
                while (elIterator.hasNext()) {
                    Page page = elIterator.next();
                    if (!checkPageIsHiddenInNav(selectedPage)) {
                        childOfPage.addLast(page);
                    }
                }
                listOfPage.addLast(selectedPage);

                navPage.setCurrent(selectedPage);
                navPage.setPath(selectedPage.getPath());
                navPage.setTitle(selectedPage.getTitle());
                navPage.setChildPage(childOfPage);
                mapOfPages.put(selectedPage.getPath(), navPage);
            }
            selectedPage = selectedPage.getParent();
        }
        mapOfPages = returnSortedByKeyReversedMap(mapOfPages);
    }

    private boolean checkPageIsHiddenInNav(Page page) {
        return page.isHideInNav();
    }

    private LinkedHashMap<Integer, NavPage> returnSortedByKeyIntReversedMap(Map<Integer, NavPage> resultMap) {
        Comparator<Map.Entry<Integer, NavPage>> byMapValues =
                (Map.Entry<Integer, NavPage> left, Map.Entry<Integer, NavPage> right) -> left.getKey().compareTo(right.getKey());
        return resultMap.entrySet().stream().sorted(byMapValues)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private LinkedHashMap<String, NavPage> returnSortedByKeyReversedMap(Map<String, NavPage> resultMap) {
        Comparator<Map.Entry<String, NavPage>> byMapValues =
                (Map.Entry<String, NavPage> left, Map.Entry<String, NavPage> right) -> left.getKey().compareTo(right.getKey());
        return resultMap.entrySet().stream().sorted(byMapValues)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public String getAvailableTitle(Page page) {
        String title = page.getPageTitle();
        if (title == null) {
            title = page.getNavigationTitle();
        }
        if (title == null) {
            title = page.getTitle();
        }
        return title;
    }

    //for We-Retail
    public String getLocaleLang() {
        String lang = rootPage.getLanguage().getLanguage();
        String path = "";
        if ("en".equals(lang)) {
            path = "/us/en";
        }
        return path;
    }

    public Page getRootPage() {
        return rootPage;
    }

    public Page getMainPage() {
        return mainPage;
    }

    public List<Page> getListOfPage() {
        return listOfPage;
    }

    public Map<String, NavPage> getMapOfPages() {
        return mapOfPages;
    }

    public Map<Integer, NavPage> getPageWithTitle() {
        return pageWithTitle;
    }
}