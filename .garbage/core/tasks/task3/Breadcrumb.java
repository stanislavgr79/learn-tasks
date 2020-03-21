package com.epam.aem.core.tasks.task3;

import java.util.*;

import com.day.cq.wcm.api.Page;

import com.adobe.cq.sightly.WCMUsePojo;

import static java.util.stream.Collectors.toMap;

public class Breadcrumb extends WCMUsePojo {

    private Page rootPage;
    private Page mainPage;
    private Page selectedPage;
    private Map<Integer, NavPage> pageWithTitle;


    @Override
    public void activate() throws Exception {
        mainPage = selectedPage = getCurrentPage();
        rootPage = mainPage.getAbsoluteParent(1);
        if (rootPage == null) {
            rootPage = getCurrentPage();
        }
        actionGetOnlyParentPageWithAvailableTitle();
    }

    private void actionGetOnlyParentPageWithAvailableTitle() {
        int depthRootPage = rootPage.getDepth();
        int depthCurrentPage = selectedPage.getDepth();
        pageWithTitle = new LinkedHashMap<>();

        for (int i = depthCurrentPage; i > depthRootPage; i--) {
            NavPage navPage = new NavPage();
            if (mainPage.getDepth() == selectedPage.getDepth()) {
                navPage.setLastPage(true);
            }
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

    private boolean checkPageIsHiddenInNav(Page page) {
        return page.isHideInNav();
    }

    private LinkedHashMap<Integer, NavPage> returnSortedByKeyIntReversedMap(Map<Integer, NavPage> resultMap) {
        Comparator<Map.Entry<Integer, NavPage>> byMapValues =
                (Map.Entry<Integer, NavPage> left, Map.Entry<Integer, NavPage> right) -> left.getKey().compareTo(right.getKey());
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
        final String WEB_APPEND_PATH = "/us/en";
        String lang = rootPage.getLanguage().getLanguage();
        String path = "";
        if ("en".equals(lang)) {
            path = WEB_APPEND_PATH;
        }
        return path;
    }

    public Page getRootPage() {
        return rootPage;
    }

    public Page getMainPage() {
        return mainPage;
    }

    public Map<Integer, NavPage> getPageWithTitle() {
        return pageWithTitle;
    }
}