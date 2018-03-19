package org.bupt.caain.pojo.vo;

public class HomeTreeAttachVo {

    private int id;
    private String text;
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

    @Override
    public String toString() {
        return "HomeTreeAttachVo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", entry_id=" + entry_id +
                ", clickable=" + clickable +
                '}';
    }
}
