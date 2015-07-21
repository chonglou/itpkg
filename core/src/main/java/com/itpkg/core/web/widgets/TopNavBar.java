package com.itpkg.core.web.widgets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-21.
 */
public class TopNavBar implements Serializable {
    public TopNavBar() {
        hotLinks = new ArrayList<>();
        barLinks = new ArrayList<>();
    }

    public void addHotLink(Link l) {
        hotLinks.add(l);
    }

    public void addBarLink(Link l) {
        barLinks.add(l);
    }

    private String title;
    private List<Link> hotLinks;
    private String barName;
    private List<Link> barLinks;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Link> getHotLinks() {
        return hotLinks;
    }

    public void setHotLinks(List<Link> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public List<Link> getBarLinks() {
        return barLinks;
    }

    public void setBarLinks(List<Link> barLinks) {
        this.barLinks = barLinks;
    }
}
