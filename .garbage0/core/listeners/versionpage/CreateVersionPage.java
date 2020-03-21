package com.epam.aem.core.listeners.versionpage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionManager;
import java.util.Iterator;

@Component(immediate = true, service = CreateVersionPage.class)
@Designate(ocd = CreateVersionPageConfig.class)
public class CreateVersionPage {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private boolean needToClearHistory = false;
    private Node nodePage = null;
    private VersionManager versionManager = null;

    @Activate
    @Modified
    public void activate(CreateVersionPageConfig  createVersionPageConfig) {
        this.needToClearHistory = createVersionPageConfig.needToClearHistory();
    }

    public void createNewVersionPage(VersionManager versionManager, Node nodePage) {
        try {
            this.nodePage = nodePage;
            this.versionManager = versionManager;

            if (needToClearHistory) {
                clearHistory();
                System.out.println("history version clear");
                logger.info("history version clear");
            }

//            Iterator<Version> cq_containerVersions = Objects.requireNonNull(versionManager.getVersionHistory(nodePage.getPath()).getAllVersions());
//            String lastVersionName = Iterators.getLast(cq_containerVersions).getName();
//            System.out.println("lastVersion name : " + lastVersionName);

            System.out.println("base version was:" + versionManager.getBaseVersion(nodePage.getPath()).getName());

            versionManager.checkpoint(nodePage.getPath());

//            cq_containerVersions = Objects.requireNonNull(versionManager.getVersionHistory(nodePage.getPath()).getAllVersions());
//            lastVersionName = Iterators.getLast(cq_containerVersions).getName();
//            System.out.println("lastVersion name : " + lastVersionName);

            System.out.println("base version now:" + versionManager.getBaseVersion(nodePage.getPath()).getName());

        } catch (RepositoryException e) {
            System.out.println("error in createNewVersionPage(String pathPage)");
            e.printStackTrace();
        }
    }

    void clearHistory() throws RepositoryException {
        VersionHistory vh = versionManager.getVersionHistory(nodePage.getPath());
        Iterator<Version> it = vh.getAllVersions();
        String baseVersion = versionManager.getBaseVersion(nodePage.getPath()).getName();
        while (it.hasNext()) {
            Version currentVersion = it.next();
            String versionName = currentVersion.getName();
            if (!versionName.equals("jcr:rootVersion") && !versionName.equals(baseVersion)) {
                vh.removeVersion(versionName);
            }
        }
    }



}
