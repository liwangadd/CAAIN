package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteVo {

    private int id;
    private String entry_name;
    private int expert_num;
    private String prize;

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

    public int getExpert_num() {
        return expert_num;
    }

    public void setExpert_num(int expert_num) {
        this.expert_num = expert_num;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
