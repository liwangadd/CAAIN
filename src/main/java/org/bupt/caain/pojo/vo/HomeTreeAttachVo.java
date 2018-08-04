package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeTreeAttachVo {

    private int id;
    private String text;
    private String file_path;
    private int entry_id;
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

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String toString() {
        return "HomeTreeAttachVo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", file_path='" + file_path + '\'' +
                ", entry_id=" + entry_id +
                ", clickable=" + clickable +
                '}';
    }
}
