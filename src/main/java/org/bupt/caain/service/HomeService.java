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

    public List<HomeTreeAwardVO> getEntriesTree() {
        List<Award> awards = awardModel.queryAll();
        List<HomeTreeAwardVO> awardVOs = new ArrayList<HomeTreeAwardVO>();
        for (Award award : awards) {
            HomeTreeAwardVO awardVO = new HomeTreeAwardVO();
            awardVO.setId(award.getId());
            awardVO.setText(award.getAward_name());
            List<Entry> entries = entryModel.queryEntriesByAwardId(award.getId());
            List<HomeTreeEntryVo> entryVos = new ArrayList<HomeTreeEntryVo>();
            for (Entry entry : entries) {
                HomeTreeEntryVo entryVo = new HomeTreeEntryVo();
                List<Attach> attaches = attachModel.queryByEntryId(entry.getId());
                List<HomeTreeAttachVo> attachVos = new ArrayList<>();
                for (Attach attach : attaches) {
                    HomeTreeAttachVo attachVo = new HomeTreeAttachVo();
                    attachVo.setText(attach.getAttach_name());
                    attachVo.setEntry_id(attach.getEntry_id());
                    attachVo.setClickable(true);
                    attachVo.setId(attach.getId());
                    attachVos.add(attachVo);
                }
                entryVo.setNodes(attachVos);
                entryVo.setText(entry.getEntry_name());
                entryVo.setClickable(false);
                entryVos.add(entryVo);
            }
            awardVO.setNodes(entryVos);
            awardVO.setClickable(false);
            awardVOs.add(awardVO);
        }
        return awardVOs;
    }

    public List<Award> getAwards() {
        return awardModel.queryAll();
    }

}
