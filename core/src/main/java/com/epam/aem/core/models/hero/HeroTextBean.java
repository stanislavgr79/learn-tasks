package com.epam.aem.core.models.hero;

public class HeroTextBean {

    /** The heading text. */
    private String headingText;

    /** The description. */
    private String description;


    //Stores the value of the dropdown
    private String drop;

    public String getDrop() {
        return drop;
    }
    public void setDrop(String drop) {
        this.drop = drop;
    }


    /**
     * @return the headingText
     */
    public String getHeadingText() {
        return headingText;
    }
    /**
     * @param headingText the headingText to set
     */
    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}