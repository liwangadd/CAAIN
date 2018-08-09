package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.BeanUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteEntryVo extends Entry {

    private int expert_count;

    public VoteEntryVo() {
    }

    public VoteEntryVo(Entry entry) {
        BeanUtils.copyProperties(entry, this);
        this.setLevel1(0);
        this.setLevel3(0);
        this.setLevel2(0);
    }

    public int getExpert_count() {
        return expert_count;
    }

    public void setExpert_count(int expert_count) {
        this.expert_count = expert_count;
    }

    @Override
    public String toString() {
        return "VoteEntryVo{" +
                super.toString() +
                "expert_count=" + expert_count +
                '}';
    }
}
