package org.bupt.caain.pojo.po;

public class Attach {

    //    自增主键
    private int id;

    //    附件名称
    private String attach_name;

    //    附件路径
    private String attach_path;

    //    所属项目ID
    private int entry_id;

    private boolean is_dir;

    private int parent_id;

    public Attach() {
    }

    public Attach(String attach_name, String attach_path, int entry_id) {
        this.attach_name = attach_name;
        this.attach_path = attach_path;
        this.entry_id = entry_id;
    }

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

    public String getAttach_path() {
        return attach_path;
    }

    public void setAttach_path(String attach_path) {
        this.attach_path = attach_path;
    }

    public boolean isIs_dir() {
        return is_dir;
    }

    public void setIs_dir(boolean is_dir) {
        this.is_dir = is_dir;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "Attach{" +
                "id=" + id +
                ", attach_name='" + attach_name + '\'' +
                ", attach_path='" + attach_path + '\'' +
                ", entry_id=" + entry_id +
                ", is_dir=" + is_dir +
                ", parent_id=" + parent_id +
                '}';
    }
}
