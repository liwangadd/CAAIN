package org.bupt.caain.pojo.po;

public class Award {

    private int id;
    private String award_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAward_name() {
        return award_name;
    }

    public void setAward_name(String award_name) {
        this.award_name = award_name;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id=" + id +
                ", award_name='" + award_name + '\'' +
                '}';
    }
}
