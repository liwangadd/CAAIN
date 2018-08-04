package org.bupt.caain.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.BeanUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteAwardVo extends Award {

    private List<VoteEntryVo> entries;

    private int expert_count;

    public VoteAwardVo(Award award) {
        BeanUtils.copyProperties(award, this);
    }

    public List<VoteEntryVo> getEntries() {
        return entries;
    }

    public void setEntries(List<VoteEntryVo> entries) {
        this.entries = entries;
    }

    public int getExpert_count() {
        return expert_count;
    }

    public void setExpert_count(int expert_count) {
        this.expert_count = expert_count;
    }
}
