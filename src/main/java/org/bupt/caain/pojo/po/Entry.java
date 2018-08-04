package org.bupt.caain.pojo.po;

public class Entry {

    //    自增主键
    private int id;

    //    项目名称
    private String entry_name;

    //    最终获奖结果
    private String entry_prize;

    private String entry_application;
    private String application_path;

    //    一等奖得票数
    private Integer level1;

    //    二等奖得票数
    private Integer level2;

    //    三等奖得票数
    private Integer level3;

    //    申报奖项的ID
    private int award_id;

    public Entry() {
    }

    public Entry(String entry_name, int award_id) {
        this.entry_name = entry_name;
        this.award_id = award_id;
    }

    public Entry(int id, String entry_name, String entry_prize, String entry_application, String application_path, Integer level1, Integer level2, Integer level3, int award_id) {
        this.id = id;
        this.entry_name = entry_name;
        this.entry_prize = entry_prize;
        this.entry_application = entry_application;
        this.application_path = application_path;
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.award_id = award_id;
    }

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

    public String getEntry_prize() {
        return entry_prize;
    }

    public void setEntry_prize(String entry_prize) {
        this.entry_prize = entry_prize;
    }

    public String getEntry_application() {
        return entry_application;
    }

    public void setEntry_application(String entry_application) {
        this.entry_application = entry_application;
    }

    public String getApplication_path() {
        return application_path;
    }

    public void setApplication_path(String application_path) {
        this.application_path = application_path;
    }

    public Integer getLevel1() {
        return level1;
    }

    public void setLevel1(Integer level1) {
        this.level1 = level1;
    }

    public Integer getLevel2() {
        return level2;
    }

    public void setLevel2(Integer level2) {
        this.level2 = level2;
    }

    public Integer getLevel3() {
        return level3;
    }

    public void setLevel3(Integer level3) {
        this.level3 = level3;
    }

    public int getAward_id() {
        return award_id;
    }

    public void setAward_id(int award_id) {
        this.award_id = award_id;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", entry_name='" + entry_name + '\'' +
                ", entry_prize='" + entry_prize + '\'' +
                ", entry_application='" + entry_application + '\'' +
                ", application_path='" + application_path + '\'' +
                ", level1=" + level1 +
                ", level2=" + level2 +
                ", level3=" + level3 +
                ", award_id=" + award_id +
                '}';
    }
}
