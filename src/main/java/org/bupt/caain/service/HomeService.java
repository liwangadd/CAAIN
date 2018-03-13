package org.bupt.caain.service;

import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
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
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AwardModel awardModel;
    @Autowired
    private EntryModel entryModel;

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
