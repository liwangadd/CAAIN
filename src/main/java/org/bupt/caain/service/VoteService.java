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

    public void votePerExpert(List<VotePerExpert> votesOfExpert, Expert expert) throws DocumentException {
        for (VotePerExpert votePerExpert : votesOfExpert) {
            EntryExpert entryExpert = entryExpertModel.queryByEntryAndExpert(votePerExpert.getEntry_id(), expert.getId());
            Entry entry = entryModel.queryById(votePerExpert.getEntry_id());
            if (entryExpert != null && entry != null) {
                entry.setLevel1(entry.getLevel1() + (votePerExpert.getLevel1() - entryExpert.getLevel1()));
                entry.setLevel2(entry.getLevel2() + (votePerExpert.getLevel2() - entryExpert.getLevel2()));
                entry.setLevel3(entry.getLevel3() + (votePerExpert.getLevel3() - entryExpert.getLevel3()));
                entryModel.updateLevelById(entry);
                entryExpertModel.update(votePerExpert, expert.getId());
                this.setShellBuildPrize(true);
            }
        }
        expertModel.updateVoteById(true, expert.getId());
        Award award = awardModel.queryById(votesOfExpert.get(0).getAward_id());
        String awardName = award.getAward_name().replaceAll("^\\d-", "");
        String filePath = pdfPath + awardName + "/" + expert.getNum() + ".pdf";
        new PrintUtils().printVoteResultPerExpert(votesOfExpert, filePath, awardName, expert.getNum());
    }

    public void preVotePerExpert(List<VotePerExpert> votesOfExpert, Expert expert) {
        for (VotePerExpert votePerExpert : votesOfExpert) {
            if (entryExpertModel.queryByEntryAndExpert(votePerExpert.getEntry_id(), expert.getId()) == null) {
                Entry entry = entryModel.queryById(votePerExpert.getEntry_id());
                if (entry != null) {
                    entry.setLevel1(entry.getLevel1() + votePerExpert.getLevel1());
                    entry.setLevel2(entry.getLevel2() + votePerExpert.getLevel2());
                    entry.setLevel3(entry.getLevel3() + votePerExpert.getLevel3());
                    entryModel.updateLevelById(entry);
                    entryExpertModel.add(votePerExpert, expert.getId());
                }
            }
        }
        expertModel.updatePreVoteById(true, expert.getId());
    }

    /**
     * 生成最终总投票结果文件
     *
     * @param awardId 最终结果文件对应的奖项ID
     */
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
    public List<VoteEntryVo> getVoteResult(int awardId, Expert expert) {
        List<Entry> entries = entryModel.queryEntriesByAwardId(awardId);
        int expertCount = expertModel.queryCount();
        return entries.stream().map((Entry entry) -> {
            VoteEntryVo voteEntryVo = new VoteEntryVo(entry);
            EntryExpert entryExpert = entryExpertModel.queryByEntryIdAndExpertId(entry.getId(), expert.getId());
            if (entryExpert != null) {
                voteEntryVo.setLevel1(entryExpert.getLevel1());
                voteEntryVo.setLevel2(entryExpert.getLevel2());
                voteEntryVo.setLevel3(entryExpert.getLevel3());
            }
            voteEntryVo.setExpert_count(expertCount);
            return voteEntryVo;
        }).collect(Collectors.toList());
    }

    public boolean isVotedDown() {
        return this.expertModel.queryNotPreVotedCount() == 0;
    }

    public Expert getExpertByIp(String ip) {
        return expertModel.queryByIp(ip);
    }

    public int getNotVotedExpertCount() {
        return expertModel.queryNotVotedCount();
    }

    public int getNotPreVotedExpertCount() {
        return expertModel.queryNotPreVotedCount();
    }

    public void setShellBuildPrize(boolean shellBuild) {
        shellBuildPrize = shellBuild;
    }

    public List<Award> getVoteAwards() {
        return awardModel.queryVoteAwards();
    }

    public VoteDataVo getVoteData(Expert expert) {
        VoteDataVo voteData = new VoteDataVo();
        voteData.setExpert(expert);
        List<Award> voteAwards = awardModel.queryVoteAwards();
        if (voteAwards != null && voteAwards.size() > 0) {
            int expertCount = expertModel.queryCount();
            List<VoteAwardVo> awardVos = voteAwards.stream().map(award -> {
                VoteAwardVo awardVo = new VoteAwardVo(award);
                List<Entry> voteEntries = entryModel.queryEntriesByAwardId(award.getId());
                List<VoteEntryVo> entries = voteEntries.stream().map(voteEntry -> {
                    VoteEntryVo voteEntryVo = new VoteEntryVo(voteEntry);
                    EntryExpert entryExpert = entryExpertModel.queryByEntryIdAndExpertId(voteEntry.getId(), expert.getId());
                    if (entryExpert != null) {
                        voteEntryVo.setLevel1(entryExpert.getLevel1());
                        voteEntryVo.setLevel2(entryExpert.getLevel2());
                        voteEntryVo.setLevel3(entryExpert.getLevel3());
                    }
                    return voteEntryVo;
                }).collect(Collectors.toList());
                if (entries.size() > 0) {
                    awardVo.setEntries(entries);
                }
                awardVo.setExpert_count(expertCount);
                return awardVo;
            }).collect(Collectors.toList());
            voteData.setVoteAwards(awardVos);
        } else {
            return null;
        }
        return voteData;
    }
}
