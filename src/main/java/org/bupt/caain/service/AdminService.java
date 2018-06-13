package org.bupt.caain.service;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryExpertModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private ExpertModel expertModel;
    @Autowired
    private AwardModel awardModel;
    @Autowired
    private EntryExpertModel entryExpertModel;
    @Autowired
    private EntryModel entryModel;
    @Autowired
    private PrintService printService;

    @Value("${pdf-dir}")
    private String pdfPath;

    // 清空投票结果
    public void clearVote() {
        expertModel.resetVote();
        entryExpertModel.deleteAll();
        entryModel.resetAll();
    }

    //生成每个专家的投票结果pdf文件
    public void printVotesPerExpert(List<VotePerExpert> votesOfExpert) throws DocumentException {
        Expert expert = expertModel.queryById(votesOfExpert.get(0).getExpert_id());
        Award award = awardModel.queryById(votesOfExpert.get(0).getAward_id());
        String awardName = award.getAward_name();
        System.out.println(awardName);
        String filePath = pdfPath + awardName + "/" + expert.getNum() + ".pdf";
        printService.printVoteResultPerExpert(votesOfExpert, filePath, awardName, expert.getNum());
    }

    /**
     * 生成每个奖项的最终投票结果pdf文件
     * @param award_id 奖项id
     * @throws DocumentException
     */
    public void printFinalPDF(int award_id) throws DocumentException {
        List<Entry> entries = entryModel.queryEntriesByAwardId(award_id);
        Award award = awardModel.queryById(award_id);
        String filePath = pdfPath + award.getAward_name() + "/final.pdf";
        printService.printVoteResult(entries, filePath, award.getAward_name());
    }

    public int startVote(int award_id){
        Award award = awardModel.queryById(award_id);
        award.setVoted(true);
        return awardModel.update(award);
    }

    // 获取所有奖项
    public List<Award> getAwards() {
        return awardModel.queryAll();
    }

    public int stopVote(int award_id) {
        Award award = awardModel.queryById(award_id);
        award.setVoted(false);
        return awardModel.update(award);
    }
}
