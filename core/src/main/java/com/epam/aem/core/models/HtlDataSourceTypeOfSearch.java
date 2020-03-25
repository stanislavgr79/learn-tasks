package com.epam.aem.core.models;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtlDataSourceTypeOfSearch extends WCMUsePojo {

    @Override
    public void activate() throws Exception {
        final ResourceResolver resolver = getResource().getResourceResolver();

//Creating the Map instance to insert the countries
        final Map<String, String> typeOfSearches = new LinkedHashMap<String, String>();

        typeOfSearches.put("QUERY_BUILDER", "queryBuilder");
        typeOfSearches.put("QUERY_MANAGER", "queryManager");

        @SuppressWarnings("unchecked")

//Creating the Datasource Object for populating the drop-down control.
                DataSource ds = new SimpleDataSource(new TransformIterator(typeOfSearches.keySet().iterator(), new Transformer() {

            @Override

//Transforms the input object into output object
            public Object transform(Object o) {
                String typeOfSearch = (String) o;

//Allocating memory to Map
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());

//Populate the Map
                vm.put("value", typeOfSearches.get(typeOfSearch));
                vm.put("text", typeOfSearch);

                return new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm);
            }
        }));

        this.getRequest().setAttribute(DataSource.class.getName(), ds);

    }
}
