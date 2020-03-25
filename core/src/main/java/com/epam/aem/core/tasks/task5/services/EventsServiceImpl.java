package com.epam.aem.core.tasks.task5.services;

import com.epam.aem.core.tasks.task5.models.EventComponent;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(immediate = true, service = EventService.class)
@ServiceDescription("Task5 EventService")
@Designate(ocd = EventsServiceImpl.EventsServiceConfig.class)
public class EventsServiceImpl implements EventService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final String SERVICE_USER_NAME = "systemuser";
    private static final String PATH_ADMIN_PAGE_NODE = "/content/we-retail/admin-page/jcr:content/root/responsivegrid";

    @ObjectClassDefinition
    public @interface EventsServiceConfig {
        @AttributeDefinition(name = "defaultSortOrder", type = AttributeType.STRING)
        String sortOrderByColumnName() default "Any";
        @AttributeDefinition(name = "defaultPageSize", type = AttributeType.INTEGER)
        int pageSize() default 5;
    }

    private String sortOrderByColumnName;
    private int pageSize;
    private Node nodeAdminPage;
    private Session session;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @SlingObject
    private ResourceResolver resourceResolver;

    @Activate
    @Modified
    public void activate(EventsServiceConfig eventsServiceConfig) {
        this.sortOrderByColumnName = eventsServiceConfig.sortOrderByColumnName();
        this.pageSize = eventsServiceConfig.pageSize();
    }

    @Override
    public EventComponent getEvent(String searchByName) throws RepositoryException {
        EventComponent component = null;
        if (initializationSingle(searchByName)) {
            NodeIterator nodeIterator = nodeAdminPage.getNodes();
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                if (node.getProperty("title").getString().equals(searchByName)) {
                    component = new EventComponent(
                            node.getProperty("title").getString(),
                            node.getProperty("description").getString(),
                            node.getProperty("eventStartDate").getDate().getTime(),
                            node.getProperty("place").getString(),
                            node.getProperty("topic").getString()
                    );
                }
            }
            return component;
        } else {
            return null;
        }
    }

    @Override
    public List<EventComponent> getAllEvents(String pathRequest) throws RepositoryException {
        buildSession();
        initializationAll(pathRequest);
        List<EventComponent> listComponents = new ArrayList<>();
        NodeIterator nodeIterator = nodeAdminPage.getNodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            EventComponent component = new EventComponent(
                    node.getProperty("title").getString(),
                    node.getProperty("description").getString(),
                    node.getProperty("eventStartDate").getDate().getTime(),
                    node.getProperty("place").getString(),
                    node.getProperty("topic").getString()

            );
            listComponents.add(component);
        }
        return returnSortedByTypeOfSorting(listComponents, sortOrderByColumnName);
    }

    private List<EventComponent> returnSortedByTypeOfSorting(List<EventComponent> listComponents, String typeOfSorting){
        switch (typeOfSorting){
            case "title": listComponents.sort(Comparator.comparing(EventComponent::getTitle)); break;
            case "description": listComponents.sort(Comparator.comparing(EventComponent::getDescription)); break;
            case "eventStartDate": listComponents.sort(Comparator.comparing(EventComponent::getEventStartDate)); break;
            case "place": listComponents.sort(Comparator.comparing(EventComponent::getPlace)); break;
            case "topic": listComponents.sort(Comparator.comparing(EventComponent::getTopic)); break;
            default: return listComponents;
        }
        return listComponents;
    }

    private boolean initializationSingle(String searchByName) {
        try {
            nodeAdminPage = session.getNode(PATH_ADMIN_PAGE_NODE);
        } catch (RepositoryException e) {
            logger.error(String.format("initialization: %s", e.getMessage()));
        }
        return searchByName != null && !searchByName.equals("");
    }

    private void initializationAll(String pathRequest) {
        Node nodeRequest;
        String name = null;
        int pagination = 0;
        try {
            nodeAdminPage = session.getNode(PATH_ADMIN_PAGE_NODE);
            nodeRequest = session.getNode(pathRequest);
            if (nodeRequest.hasProperty("columnNameForSorting")) {
                name = nodeRequest.getProperty("columnNameForSorting").getString();
            }
            pagination = Integer.parseInt(nodeRequest.getProperty("paginationElements").getString());
        } catch (RepositoryException e) {
            logger.error(String.format("initialization: %s", e.getMessage()));
        }
        if (name != null && !name.equals("")) {
            sortOrderByColumnName = name;
        }
        if (pagination > 0) {
            pageSize = pagination;
        }
    }

    protected void buildSession() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (org.apache.sling.api.resource.LoginException e) {
            logger.error(String.format("buildSession: %s", e.getMessage()));
        }
        session = resourceResolver.adaptTo(Session.class);
    }
}
