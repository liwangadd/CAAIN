package org.bupt.caain.service;

import org.bupt.caain.model.EntryExpertModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.po.Expert;
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

    private boolean shellBuildPrize = true;

    public List<VoteVo> getEntriesForType(int awardId) {
        List<Entry> entries = entryModel.queryEntriesByAwardId(awardId);
        if (null != entries && entries.size() > 0) {
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
        } else {
            return null;
        }
    }

    public boolean votePerExpert(List<EntryExpert> entryExperts) {
        Expert expert = expertModel.queryById(entryExperts.get(0).getExpert_id());
        if (expert != null && expert.getVoted() != 1) {
            for (EntryExpert entryExpert : entryExperts) {
                if (entryExpertModel.queryByEntryAndExpert(entryExpert) && entryExpert.getExpert_id() != 0) {
                    entryExpertModel.insert(entryExpert);
                    Entry entry = entryModel.queryById(entryExpert.getEntry_id());
                    entry.setLevel1(entry.getLevel1() + entryExpert.getLevel1());
                    entry.setLevel2(entry.getLevel2() + entryExpert.getLevel2());
                    entry.setLevel3(entry.getLevel3() + entryExpert.getLevel3());
                    entryModel.updateLevelById(entry);
                }
            }
            expertModel.updateById(expert.getId());
            return true;
        }
        return false;
    }

    public void clearVote() {
        shellBuildPrize = true;
        expertModel.resetVote();
    }

    public void buildVoteResult(int awardId) {
        if (shellBuildPrize) {
            entryModel.buildPrizeField(awardId);
            shellBuildPrize = false;
        }
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

    public Expert getExpertByIp(String ip) {
        return expertModel.queryByIp(ip);
    }

    public int getUnvotedExpertCount() {
        return expertModel.queryCount() - expertModel.queryVotedCount();
    }

}
