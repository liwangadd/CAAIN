package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeTreeAwardVO {

    private int id;
    private String text;
    private List<HomeTreeEntryVo> nodes;
    private boolean clickable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<HomeTreeEntryVo> getNodes() {
        return nodes;
    }

    public void setNodes(List<HomeTreeEntryVo> nodes) {
        this.nodes = nodes;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public String toString() {
        return "HomeTreeAwardVO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", nodes=" + nodes +
                ", clickable=" + clickable +
                '}';
    }
}
