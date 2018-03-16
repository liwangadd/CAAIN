package org.bupt.caain.pojo.po;

public class Entry {
    private int id;
    private String entry_name;
    private String entry_prize;
    private int level1;
    private int level2;
    private int level3;
    private int award_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntry_name() {
        return entry_name;
    }

    public void setEntry_name(String entry_name) {
        this.entry_name = entry_name;
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

    public int getAward_id() {
        return award_id;
    }

    public void setAward_id(int award_id) {
        this.award_id = award_id;
    }

    public String getEntry_prize() {
        return entry_prize;
    }

    public void setEntry_prize(String entry_prize) {
        this.entry_prize = entry_prize;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", entry_name='" + entry_name + '\'' +
                ", entry_prize='" + entry_prize + '\'' +
                ", level1=" + level1 +
                ", level2=" + level2 +
                ", level3=" + level3 +
                ", award_id=" + award_id +
                '}';
    }
}
