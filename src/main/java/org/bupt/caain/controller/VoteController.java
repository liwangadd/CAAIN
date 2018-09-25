package org.bupt.caain.controller;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.VoteDataVo;
import org.bupt.caain.pojo.vo.VoteEntryVo;
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
@RequestMapping("vote")
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
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getVotePage() {
        return "vote";
    }

    /**
     * 提交投票结果
     *
     * @param votesOfExpert 专家投票结果数据
     * @return 是否投票成功
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
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
        } else if (expert.isVoted()) {
            return CommonResult.failure("请勿重复投票");
        } else if (!expert.isPre_voted()) {
            return CommonResult.failure("请先进行预投票");
        } else {
            try {
                voteService.votePerExpert(votesOfExpert, expert);
                return CommonResult.success("投票成功");
            } catch (DocumentException e) {
                log.error("专家id:\t{}投票结果PDF文件生成失败", votesOfExpert.get(0).getExpert_id());
                e.printStackTrace();
                return CommonResult.failure("投票失败，请重新投票");
            }
        }
    }

    @RequestMapping(value = "pre", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult preVotePerExpert(@RequestBody List<VotePerExpert> votesOfExpert, HttpServletRequest request) {
        log.info(votesOfExpert.toString());
        if (votesOfExpert.size() == 0) {
            return CommonResult.failure("预投票信息不完整");
        }
        String ip = NetworkUtils.getIpAddr(request);
        Expert expert = voteService.getExpertByIp(ip);
        if (expert == null) {
            log.error("非法用户参与预投票，ip:\t{}", ip);
            return CommonResult.failure("您没有预投票资格");
        } else if (expert.isPre_voted()) {
            return CommonResult.failure("请勿重复预投票");
        } else if (expert.isVoted()) {
            return CommonResult.failure("您已投票，不可进行预投票");
        } else {
            voteService.preVotePerExpert(votesOfExpert, expert);
            return CommonResult.success("预投票成功");
        }
    }

    /**
     * 获取投票页面数据
     *
     * @param request 请求request，用户获取客户端IP
     * @return 数据
     */
    @RequestMapping(value = "data", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteData(HttpServletRequest request) {
        String ip = NetworkUtils.getIpAddr(request);
        log.info("当前访问专家ip：\t{}", ip);

        Expert expert = voteService.getExpertByIp(ip);
        if (expert == null) {
            return CommonResult.failure("您没有投票资格");
        } else {
            VoteDataVo voteData = voteService.getVoteData(expert);
            if (voteData == null) {
                return CommonResult.failure("投票还未开始");
            } else {
                return CommonResult.success("获取数据成功", voteData);
            }
        }
    }

    /**
     * 获取投票结果
     *
     * @param awardId 投票奖项ID
     * @return 数据
     */
    @RequestMapping(value = "/result/{award_id}", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteResult(@PathVariable("award_id") int awardId, HttpServletRequest request) {
        String ip = NetworkUtils.getIpAddr(request);
        if (!voteService.isVotedDown()) {
            return CommonResult.failure("投票还未结束");
        }
        Expert expert = voteService.getExpertByIp(ip);
        voteService.buildVoteResult(awardId);
        List<VoteEntryVo> voteResult = voteService.getVoteResult(awardId, expert);
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
        int count = voteService.getNotVotedExpertCount();
        content.put("count", count);
        return CommonResult.success("获取未投票专家人数", content);
    }

    /**
     * 获取未预投票专家人数
     *
     * @return 未预投票专家人数
     */
    @RequestMapping(value = "/pre/unvoted", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getNotPreVotedExpertCount() {
        Map<String, Object> content = new HashMap<>();
        int count = voteService.getNotPreVotedExpertCount();
        content.put("count", count);
        return CommonResult.success("获取未投票专家人数", content);
    }

}
