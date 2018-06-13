package org.bupt.caain.service;

import org.bupt.caain.model.AttachModel;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.pojo.po.Attach;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.HomeTreeAttachVo;
import org.bupt.caain.pojo.vo.HomeTreeAwardVO;
import org.bupt.caain.pojo.vo.HomeTreeEntryContent;
import org.bupt.caain.pojo.vo.HomeTreeEntryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    @Autowired
    private AwardModel awardModel;
    @Autowired
    private EntryModel entryModel;
    @Autowired
    private AttachModel attachModel;

    /**
     * 层次获取所有奖项
     * @return
     */
    public List<HomeTreeAwardVO> getEntriesTree() {
        // 获取所有奖项
        List<Award> awards = awardModel.queryAll();
        List<HomeTreeAwardVO> awardVOs = new ArrayList<HomeTreeAwardVO>();
        // 获取奖项下所有作品
        for (Award award : awards) {
            HomeTreeAwardVO awardVO = new HomeTreeAwardVO();
            awardVO.setId(award.getId());
            awardVO.setText(award.getAward_name());
            List<Entry> entries = entryModel.queryEntriesByAwardId(award.getId());
            List<HomeTreeEntryVo> entryVos = new ArrayList<HomeTreeEntryVo>();

            // 获取作品附件
            for (Entry entry : entries) {
                HomeTreeEntryVo entryVo = new HomeTreeEntryVo();
                List<HomeTreeEntryContent> entryContents = new ArrayList<>();

                List<Attach> attaches = attachModel.queryByEntryId(entry.getId());
                List<HomeTreeAttachVo> attachVos = new ArrayList<>();
                for (Attach attach : attaches) {
                    HomeTreeAttachVo attachVo = new HomeTreeAttachVo();
                    attachVo.setText(attach.getAttach_name());
                    attachVo.setEntry_id(attach.getEntry_id());
                    attachVo.setClickable(true);
                    attachVo.setFile_path(attach.getAttach_path());
                    attachVo.setId(attach.getId());
                    attachVos.add(attachVo);
                }
                entryContents.add(new HomeTreeEntryContent(entry.getEntry_application(), true, entry.getApplication_path(),null));
                entryContents.add(new HomeTreeEntryContent("附件", false, "", attachVos));
                entryVo.setText(entry.getEntry_name());
                entryVo.setClickable(false);
                entryVo.setNodes(entryContents);
                entryVos.add(entryVo);
            }
            awardVO.setNodes(entryVos);
            awardVO.setClickable(false);
            awardVOs.add(awardVO);
        }
        return awardVOs;
    }

    public List<Award> getVoteAwards(){
        return awardModel.queryVoteAwards();
    }

}
