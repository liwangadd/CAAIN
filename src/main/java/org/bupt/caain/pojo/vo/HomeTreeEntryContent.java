package org.bupt.caain.pojo.vo;

import java.util.List;

public class HomeTreeEntryContent{
    private String text;
    private boolean clickable;
    private String file_path;
    private List<HomeTreeAttachVo> nodes;

    public HomeTreeEntryContent(String text, boolean clickable, String file_path, List<HomeTreeAttachVo> nodes) {
        this.text = text;
        this.clickable = clickable;
        this.file_path = file_path;
        this.nodes = nodes;
    }

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

    public List<HomeTreeAttachVo> getNodes() {
        return nodes;
    }

    public void setNodes(List<HomeTreeAttachVo> nodes) {
        this.nodes = nodes;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String toString() {
        return "HomeTreeEntryContent{" +
                "text='" + text + '\'' +
                ", clickable=" + clickable +
                ", file_path='" + file_path + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
