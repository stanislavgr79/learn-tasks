package com.epam.aem.core.models.hero;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeroTextComponent extends WCMUsePojo {

    private Page rootPage;

    /** The hero text bean. */
    private HeroTextBean heroTextBean = null;

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void activate() throws Exception {
        rootPage = getCurrentPage().getAbsoluteParent(1);
        heroTextBean = new HeroTextBean();

        //Get the values that the author entered into the AEM dialog
        String heading = getProperties().get("jcr:Heading", "");
        String description = getProperties().get("jcr:description","");
        String drop = getProperties().get("jcr:drop", "");

        heroTextBean.setHeadingText(heading);
        heroTextBean.setDescription(description);
        heroTextBean.setDrop(drop);

    }


    public HeroTextBean getHeroTextBean() {
        return this.heroTextBean;
    }
}
