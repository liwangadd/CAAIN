package org.bupt.caain.service;

import org.bupt.caain.model.AttachModel;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.pojo.po.Attach;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.vo.HomeTreeAttachVo;
import org.bupt.caain.pojo.vo.HomeTreeAwardVO;
import org.bupt.caain.pojo.vo.HomeTreeEntryContent;
import org.bupt.caain.pojo.vo.HomeTreeEntryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    private final AwardModel awardModel;
    private final EntryModel entryModel;
    private final AttachModel attachModel;

    @Autowired
    public HomeService(AwardModel awardModel, EntryModel entryModel, AttachModel attachModel) {
        this.awardModel = awardModel;
        this.entryModel = entryModel;
        this.attachModel = attachModel;
    }

    /**
     * 层次获取所有奖项
     *
     * @return 首页数据
     */
    public List<HomeTreeAwardVO> getEntriesTree() {
        // 获取所有奖项
        List<Award> awards = awardModel.queryAll();
        List<HomeTreeAwardVO> awardVOs = new ArrayList<>();
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
                // 添加申请书item
                entryContents.add(new HomeTreeEntryContent(entry.getEntry_application(), true,
                        entry.getApplication_path(), null));

                List<Attach> attaches = attachModel.queryByEntryIdAndParentId(entry.getId(), -1);

                //层级获取所有附件·
                List<HomeTreeEntryContent> attachVos = new ArrayList<>();
                for (Attach attach : attaches) {
                    System.out.println(attach);
                    attachVos.add(getEntryContents(attach, entry.getId()));
                }
                /*for (Attach attach : attaches) {
                    HomeTreeAttachVo attachVo = new HomeTreeAttachVo();
                    attachVo.setText(attach.getAttach_name());
                    attachVo.setEntry_id(attach.getEntry_id());
                    attachVo.setClickable(true);
                    attachVo.setFile_path(attach.getAttach_path());
                    attachVo.setId(attach.getId());
                    attachVos.add(attachVo);
                }*/
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

    private HomeTreeEntryContent getEntryContents(Attach attach, int entryId) {
        if (attach.isIs_dir()) {
            List<Attach> attaches = this.attachModel.queryByEntryIdAndParentId(entryId, attach.getId());
            List<HomeTreeEntryContent> contents = new ArrayList<>();
            for (Attach childAttach : attaches) {
                contents.add(getEntryContents(childAttach, entryId));
            }
            return new HomeTreeEntryContent(attach.getAttach_name(), false, "", contents);
        } else {
            return new HomeTreeEntryContent(attach.getAttach_name(), true, attach.getAttach_path(), null);
        }
    }

}
