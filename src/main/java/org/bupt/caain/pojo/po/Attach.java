package org.bupt.caain.pojo.po;

public class Attach {
    private int id;
    private String attach_name;
    private int entry_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttach_name() {
        return attach_name;
    }

    public void setAttach_name(String attach_name) {
        this.attach_name = attach_name;
    }

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    @Override
    public String toString() {
        return "Attach{" +
                "id=" + id +
                ", attach_name='" + attach_name + '\'' +
                ", entry_id=" + entry_id +
                '}';
    }
}
