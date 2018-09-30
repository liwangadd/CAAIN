package org.bupt.caain.service;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryExpertModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.utils.CommonResult;
import org.bupt.caain.utils.PrintUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final ExpertModel expertModel;
    private final AwardModel awardModel;
    private final EntryExpertModel entryExpertModel;
    private final EntryModel entryModel;
    private final VoteService voteService;

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Value("${pdf-dir}")
    private String pdfPath;

    @Autowired
    public AdminService(ExpertModel expertModel, AwardModel awardModel, EntryExpertModel entryExpertModel, EntryModel entryModel, VoteService voteService) {
        this.expertModel = expertModel;
        this.awardModel = awardModel;
        this.entryExpertModel = entryExpertModel;
        this.entryModel = entryModel;
        this.voteService = voteService;
    }

    /**
     * 清空投票结果
     */
    public void clearVote() {
        log.info("清空投票结果");
        expertModel.resetVote();
        entryExpertModel.deleteAll();
        entryModel.resetAll();
        voteService.setShellBuildPrize(true);
    }

    /**
     * 生成每个奖项的最终投票结果pdf文件
     *
     * @param award_id 奖项id
     * @throws DocumentException PDF文件生成异常
     */
    public void printFinalPDF(int award_id) throws DocumentException {
        log.info("打印作品获奖信息");
        List<Entry> entries = entryModel.queryEntriesByAwardId(award_id);
        Award award = awardModel.queryById(award_id);
        String filePath = pdfPath + award.getAward_name().replaceAll("^\\d-", "") + "/final.pdf";
        new PrintUtils().printVoteResult(entries, filePath, award.getAward_name().replaceAll("^\\d-", ""));
    }

    public int startVote(int award_id) {
        Award award = awardModel.queryById(award_id);
        log.warn("开启奖项\t{}投票", award.getAward_name());
        if (award.isVoted()) {
            return -1;
        } else {
            expertModel.resetVote();
            List<Award> votedAwards = awardModel.queryVoteAwards();
            if (votedAwards != null) {
                votedAwards.stream().filter(Award::isVoted).forEach(votedAward -> {
                    votedAward.setVoted(false);
                    awardModel.update(votedAward);
                });
            }
            voteService.setShellBuildPrize(true);
            List<Integer> entryIds = entryModel.queryIdsByAwardId(award_id);
            entryExpertModel.deleteByEntryIds(entryIds);
            award.setVoted(true);
            return awardModel.update(award);
        }
    }

    /**
     * 获取所有奖项
     */
    public List<Award> getAwards() {
        return awardModel.queryAll();
    }

    public Award getAward(int awardId) {
        return awardModel.queryById(awardId);
    }

    public void stopVote(int award_id) {
        Award award = awardModel.queryById(award_id);
        award.setVoted(false);
        awardModel.update(award);
    }

    public List<Entry> getVoteResult(int awardId) {
        return this.entryModel.queryVotedEntries();

    }
}
