package org.bupt.caain.controller;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.VoteDataVo;
import org.bupt.caain.pojo.vo.VoteEntryVo;
import org.bupt.caain.pojo.vo.VoteVo;
import org.bupt.caain.service.VoteService;
import org.bupt.caain.utils.CommonResult;
import org.bupt.caain.utils.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VoteController {

    private final VoteService voteService;

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * 获取投票页面
     *
     * @return 投票页面名称
     */
    @RequestMapping(value = "vote", method = RequestMethod.GET)
    public String getVotePage() {
        return "vote";
    }

    /**
     * 提交投票结果
     *
     * @param votesOfExpert 专家投票结果数据
     * @return 是否投票成功
     */
    @RequestMapping(value = "vote", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult votePerExpert(@RequestBody List<VotePerExpert> votesOfExpert, HttpServletRequest request) {
        log.info(votesOfExpert.toString());
        if (votesOfExpert.size() == 0) {
            return CommonResult.failure("投票信息不完整");
        }
        String ip = NetworkUtils.getIpAddr(request);
        Expert expert = voteService.getExpertByIp(ip);
        if (expert == null) {
            log.error("非法用户参与投票，ip:\t{}", ip);
            return CommonResult.failure("用户非法");
        }
        try {
            boolean isSuccess = voteService.votePerExpert(votesOfExpert, expert);
            if (isSuccess) {
                return CommonResult.success("投票成功");
            } else {
                return CommonResult.failure("请勿重复投票");
            }
        } catch (DocumentException e) {
            log.error("专家id:\t{}投票结果PDF文件生成失败", votesOfExpert.get(0).getExpert_id());
            e.printStackTrace();
            return CommonResult.failure("投票失败，请重新投票");
        }
    }

    /**
     * 获取投票页面数据
     *
     * @param request 请求request，用户获取客户端IP
     * @return 数据
     */
    @RequestMapping(value = "voteData", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteData(HttpServletRequest request) {
        String ip = NetworkUtils.getIpAddr(request);
        log.info("当前访问专家ip：\t{}", ip);
        VoteDataVo voteData = voteService.getVoteData(ip);
        if (voteData.getReason() != null) {
            return CommonResult.failure(voteData.getReason());
        } else {
            return CommonResult.success("获取数据成功", voteData);
        }
    }

    /**
     * 获取投票结果
     *
     * @param awardId 投票奖项ID
     * @return 数据
     */
    @RequestMapping(value = "vote/result/{award_id}", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteResult(@PathVariable("award_id") int awardId) {
        if (!voteService.isVotedDown()) {
            return CommonResult.failure("投票还未结束");
        }
        voteService.buildVoteResult(awardId);
        List<VoteEntryVo> voteResult = voteService.getVoteResult(awardId);
        return CommonResult.success("获取投票结果", voteResult);
    }


    /**
     * 获取未投票专家人数
     *
     * @return 未投票专家人数
     */
    @RequestMapping(value = "/unvoted", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getUnvotedExpertCount() {
        Map<String, Object> content = new HashMap<>();
        int count = voteService.getUnvotedExpertCount();
        content.put("count", count);
        return CommonResult.success("获取未投票专家人数", content);
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getAwards() {
        List<Award> awards = voteService.getVoteAwards();
        if (null != awards && awards.size() > 0) {
            return CommonResult.success("获取全部奖项", awards);
        } else {
            return CommonResult.failure("投票还未开始");
        }
    }

    @RequestMapping(value = "expert", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getExpertByIp(HttpServletRequest request) {
        String ip = NetworkUtils.getIpAddr(request);
        System.out.println(ip);
        Expert expert = voteService.getExpertByIp(ip);
        if (null != expert) {
            return CommonResult.success("获取专家编号成功", expert);
        } else {
            return CommonResult.failure("您没有投票资格");
        }
    }

    @RequestMapping(value = "entries/{award_id}", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteForType(@PathVariable("award_id") int awardId) {
        List<VoteVo> voteVos = voteService.getEntriesForType(awardId);
        if (null != voteVos) {
            return CommonResult.success("获取奖项参赛作品成功", voteVos);
        } else {
            return CommonResult.failure("没有参与该奖项评审的作品");
        }
    }

}
