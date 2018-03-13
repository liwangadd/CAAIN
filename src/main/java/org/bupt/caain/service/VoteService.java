package org.bupt.caain.service;

import org.bupt.caain.model.EntryExpertModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.vo.VoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {

    @Autowired
    private ExpertModel expertModel;
    @Autowired
    private EntryModel entryModel;
    @Autowired
    private EntryExpertModel entryExpertModel;

    public List<VoteVo> getEntriesForType(int awardId) {
        List<Entry> entries = entryModel.queryEntriesByAwardId(awardId);
        Integer count = expertModel.queryCount();
        List<VoteVo> voteVos = new ArrayList<VoteVo>();
        for (Entry entry : entries) {
            VoteVo voteVo = new VoteVo();
            voteVo.setId(entry.getId());
            voteVo.setEntry_name(entry.getEntry_name());
            voteVo.setExpert_num(count);
            voteVos.add(voteVo);
        }
        return voteVos;
    }

    public void votePerExpert(EntryExpert entryExpert) {
        entryExpertModel.insert(entryExpert);
        Entry entry = entryModel.queryById(entryExpert.getEntry_id());
        entry.setLevel1(entry.getLevel1() + entryExpert.getLevel1());
        entry.setLevel2(entry.getLevel2() + entryExpert.getLevel2());
        entry.setLevel3(entry.getLevel3() + entryExpert.getLevel3());
        entryModel.updateLevelById(entry);
        expertModel.updateById(entryExpert.getExpert_id());
    }

    public void clearVote() {
        expertModel.resetVote();
    }

    public void buildVoteResult(int awardId) {
        entryModel.buildPrizeField(awardId);
    }

    /**
     * 获取最终投票结果
     *
     * @param awardId 奖项ID
     * @return
     */
    public List<VoteVo> getVoteResult(int awardId) {
        List<Entry> entries = entryModel.queryEntriesByAwardId(awardId);
        List<VoteVo> voteVos = new ArrayList<VoteVo>();
        int expertCount = expertModel.queryCount();
        for (Entry entry : entries) {
            VoteVo voteVo = new VoteVo();
            voteVo.setId(entry.getId());
            voteVo.setExpert_num(expertCount);
            voteVo.setEntry_name(entry.getEntry_name());
            voteVo.setPrize(entry.getPrize());
            voteVos.add(voteVo);
        }
        return voteVos;
    }

    public boolean isVotedDown() {
        int count = expertModel.queryCount();
        int votedCount = expertModel.queryVotedCount();
        return count == votedCount;
    }

}
