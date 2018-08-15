package org.bupt.caain.service;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryExpertModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.VoteAwardVo;
import org.bupt.caain.pojo.vo.VoteDataVo;
import org.bupt.caain.pojo.vo.VoteEntryVo;
import org.bupt.caain.utils.PrintUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final AwardModel awardModel;
    private final ExpertModel expertModel;
    private final EntryModel entryModel;
    private final EntryExpertModel entryExpertModel;

    private static final Logger log = LoggerFactory.getLogger(VoteService.class);

    @Value("${pdf-dir}")
    private String pdfPath;

    private static boolean shellBuildPrize = true;

    @Autowired
    public VoteService(AwardModel awardModel, ExpertModel expertModel, EntryModel entryModel, EntryExpertModel entryExpertModel) {
        this.awardModel = awardModel;
        this.expertModel = expertModel;
        this.entryModel = entryModel;
        this.entryExpertModel = entryExpertModel;
    }

    public boolean votePerExpert(List<VotePerExpert> votesOfExpert, Expert expert) throws DocumentException {
//        Expert expert = expertModel.queryById(votesOfExpert.get(0).getExpert_id());
        if (expert != null && expert.getVoted() != 1) {
            for (VotePerExpert votePerExpert : votesOfExpert) {
                if (entryExpertModel.queryByEntryAndExpert(votePerExpert) == null && votePerExpert.getExpert_id() > 0) {
                    Entry entry = entryModel.queryById(votePerExpert.getEntry_id());
                    if (entry != null) {
                        entry.setLevel1(entry.getLevel1() + votePerExpert.getLevel1());
                        entry.setLevel2(entry.getLevel2() + votePerExpert.getLevel2());
                        entry.setLevel3(entry.getLevel3() + votePerExpert.getLevel3());
                        entryModel.updateLevelById(entry);
                        entryExpertModel.add(votePerExpert);
                    }
                }
            }
            expertModel.updateById(true, expert.getId());
            Award award = awardModel.queryById(votesOfExpert.get(0).getAward_id());
            String awardName = award.getAward_name().replaceAll("^\\d-", "");
            String filePath = pdfPath + awardName + "/" + expert.getNum() + ".pdf";
            new PrintUtils().printVoteResultPerExpert(votesOfExpert, filePath, awardName, expert.getNum());
            return true;
        }
        return false;
    }

    public void buildVoteResult(int awardId) {
        if (shellBuildPrize) {
            log.info("生成作品最终获奖信息");
            entryModel.buildPrizeField(awardId);
            shellBuildPrize = false;
        }
    }

    /**
     * 获取最终投票结果
     *
     * @param awardId 奖项ID
     * @return 该奖项作品的最终获奖结果
     */
    public List<VoteEntryVo> getVoteResult(int awardId) {
        List<Entry> entries = entryModel.queryEntriesByAwardId(awardId);
        int expertCount = expertModel.queryCount();
        return entries.stream().map((Entry entry) -> {
            VoteEntryVo voteEntryVo = new VoteEntryVo(entry);
            voteEntryVo.setExpert_count(expertCount);
            return voteEntryVo;
        }).collect(Collectors.toList());
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

    public void setShellBuildPrize(boolean shellBuild) {
        shellBuildPrize = shellBuild;
    }

    public List<Award> getVoteAwards() {
        return awardModel.queryVoteAwards();
    }

    public VoteDataVo getVoteData(String ip) {
        VoteDataVo voteData = new VoteDataVo();
        Expert expert = expertModel.queryByIp(ip);
        if (expert != null) {
            voteData.setExpert(expert);
        } else {
            voteData.setReason("您没有投票资格");
            return voteData;
        }
        List<Award> voteAwards = awardModel.queryVoteAwards();
        List<VoteAwardVo> voteAwardVos = new ArrayList<>();
//        log.error("award count: {}", voteAwards.size());
        if (voteAwards != null && voteAwards.size() > 0) {
            int expertCount = expertModel.queryCount();
            for (Award voteAward : voteAwards) {
                List<Entry> entries = entryModel.queryEntriesByAwardId(voteAward.getId());
                List<VoteEntryVo> voteEntries = entries.stream().map(entry -> {
                    EntryExpert entryExpert = entryExpertModel.queryByEntryIdAndExpertId(entry.getId(),expert.getId());
                    VoteEntryVo voteEntry = new VoteEntryVo();
                    voteEntry.setId(entry.getId());
                    voteEntry.setEntry_name(entry.getEntry_name());
                    voteEntry.setAward_id(entry.getAward_id());
                    if(entryExpert!=null) {
                        voteEntry.setLevel1(entryExpert.getLevel1());
                        voteEntry.setLevel2(entryExpert.getLevel2());
                        voteEntry.setLevel3(entryExpert.getLevel3());
                    }else{
                        voteEntry.setLevel3(0);
                        voteEntry.setLevel2(0);
                        voteEntry.setLevel1(0);
                    }
                    return voteEntry;
                }).collect(Collectors.toList());
//                List<VoteEntryVo> entries = entryModel.queryEntriesWithPrize(voteAward.getId(), expert.getId());
                log.error(entries.toString());
//                entryModel.queryEntriesWithPrize(voteAward.getId(), expert.getId());
                if (voteEntries.size() > 0) {
                    VoteAwardVo awardVo = new VoteAwardVo(voteAward);
                    awardVo.setEntries(voteEntries);
                    awardVo.setExpert_count(expertCount);
                    voteAwardVos.add(awardVo);
                }
            }
            voteData.setVoteAwards(voteAwardVos);
        } else {
            voteData.setReason("投票还未开始");
            return voteData;
        }
        return voteData;
    }
}
