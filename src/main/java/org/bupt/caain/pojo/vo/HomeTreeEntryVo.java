package org.bupt.caain.pojo.vo;

public class HomeTreeEntryVo {

    private String text;
    private boolean clickable;

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

    @Override
    public String toString() {
        return "EntryVo{" +
                "text='" + text + '\'' +
                '}';
    }
}
