package org.bupt.caain.pojo.jo;

import org.bupt.caain.pojo.po.EntryExpert;

public class VotePerExpert extends EntryExpert {
    private String entry_name;
    private String entry_prize;
    private int award_id;

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

    public int getAward_id() {
        return award_id;
    }

    public void setAward_id(int award_id) {
        this.award_id = award_id;
    }

    @Override
    public String toString() {
        return "VotePerExpert{" +
                "entry_name='" + entry_name + '\'' +
                ", entry_prize='" + entry_prize + '\'' +
                ", award_id=" + award_id +
                '}';
    }
}
