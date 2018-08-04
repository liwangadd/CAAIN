package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeTreeEntryVo {

    private String text;
    private boolean clickable;
    private List<HomeTreeEntryContent> nodes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public List<HomeTreeEntryContent> getNodes() {
        return nodes;
    }

    public void setNodes(List<HomeTreeEntryContent> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "HomeTreeEntryVo{" +
                "text='" + text + '\'' +
                ", clickable=" + clickable +
                ", nodes=" + nodes +
                '}';
    }
}
