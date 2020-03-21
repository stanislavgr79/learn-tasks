package com.epam.aem.core.tasks.task1.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ParsysTableComponent {

    @ValueMapValue
    @Default(intValues = 0)
    private int row;

    @ValueMapValue
    @Default(intValues = 0)
    private int column;

    private List<List<String>> table;
    private String style = null;
    boolean hasContent;


    @PostConstruct
    protected void init() {
        if (hasContent()) {
            buildParsysTableByRowAndColumn();
        }
    }

    private boolean hasContent() {
        checkRowAndColumn();
        return hasContent;
    }

    private void checkRowAndColumn (){
        if (row < 1 || column < 1) {
            row = 0;
            column = 0;
            hasContent = false;
        } else {
            hasContent = true;
        }
    }

    private void buildParsysTableByRowAndColumn() {
        table = new ArrayList<>();
        for (int i = 1; i < row + 1; i++) {
            List<String> rows = new ArrayList<>();
            for (int y = 1; y < column + 1; y++) {
                rows.add("par_r" + i + "_c" + y);
            }
            table.add(rows);
        }
        style = "grid-template-columns: repeat(" + column + ", 1fr)";
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public List<List<String>> getTable() {
        return table;
    }

    public String getStyle() {
        return style;
    }
}
