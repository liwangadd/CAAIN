package org.bupt.caain.pojo.po;

public class EntryExpert {

    //    自增主键
    private int id;

    //    审评项目ID
    private int entry_id;

    //    专家ID
    private int expert_id;

    //    一等奖
    private int level1;

    //    二等奖
    private int level2;

    //    三等奖
    private int level3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    public int getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(int expert_id) {
        this.expert_id = expert_id;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    @Override
    public String toString() {
        return "EntryExpert{" +
                "id=" + id +
                ", entry_id=" + entry_id +
                ", expert_id=" + expert_id +
                ", level1=" + level1 +
                ", level2=" + level2 +
                ", level3=" + level3 +
                '}';
    }
}
