package org.bupt.caain.controller;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.service.AdminService;
import org.bupt.caain.service.VoteService;
import org.bupt.caain.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminPage() {
        return "admin";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getAwards() {
        List<Award> awards = adminService.getAwards();
        if (null != awards && awards.size() > 0) {
            return CommonResult.success("获取全部奖项", awards);
        } else {
            return CommonResult.failure("获取全部奖项失败");
        }
    }

    @RequestMapping(value = "vote/clear", method = RequestMethod.PUT)
    public @ResponseBody
    CommonResult clear() {
        adminService.clearVote();
        voteService.setShellBuildPrize(true);
        return CommonResult.success("成功清空投票结果");
    }

    @RequestMapping(value = "/pdf/{award_id}", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult buildFinalPDF(@PathVariable int award_id) {
        if (!voteService.isVotedDown()) {
            return CommonResult.failure("投票还未结束");
        }
        try {
            adminService.printFinalPDF(award_id);
            adminService.stopVote(award_id);
        } catch (DocumentException e) {
            return CommonResult.failure("PDF文件生成失败");
        }
        return CommonResult.success("最终文件生成成功");
    }

    @PostMapping(value = "start_vote/{award_id}")
    public @ResponseBody CommonResult startVote(@PathVariable int award_id){
        int affectRows = adminService.startVote(award_id);
        if(affectRows>0){
            return CommonResult.success("开启投票成功");
        }else{
            return CommonResult.failure("开启投票失败");
        }
    }

}
