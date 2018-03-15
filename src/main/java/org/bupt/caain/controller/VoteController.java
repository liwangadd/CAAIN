package org.bupt.caain.controller;

import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.HomeTreeAwardVO;
import org.bupt.caain.pojo.vo.VoteVo;
import org.bupt.caain.service.HomeService;
import org.bupt.caain.service.VoteService;
import org.bupt.caain.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "vote", method = RequestMethod.GET)
    public String getVotePage() {
        return "vote";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getAwards() {
        List<Award> awards = homeService.getAwards();
        if (null != awards && awards.size() > 0) {
            return CommonResult.success("获取全部奖项", awards);
        } else {
            return CommonResult.failure("获取奖项失败");
        }
    }

    @RequestMapping(value = "expert", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getExpertByIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forward-For");
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
        int index = ip.indexOf(",");
        if (index != -1) {
            ip = ip.substring(0, index);
        }
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

    @RequestMapping(value = "vote", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult votePerExpert(@RequestBody List<EntryExpert> entryExperts) {
        System.out.println(entryExperts);
        boolean isSuccess = voteService.votePerExpert(entryExperts);
        if (isSuccess) {
            return CommonResult.success("投票成功");
        } else {
            return CommonResult.failure("请勿重复投票");
        }
    }

    @RequestMapping(value = "vote/result/{award_id}", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getVoteResult(@PathVariable("award_id") int awardId) {
        if (!voteService.isVotedDown()) {
            return CommonResult.failure("投票还未结束");
        }
        voteService.buildVoteResult(awardId);
        List<VoteVo> voteResult = voteService.getVoteResult(awardId);
        return CommonResult.success("获取投票结果", voteResult);
    }

    @RequestMapping(value = "vote/clear", method = RequestMethod.GET)
    public CommonResult clear(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        if ("bupt".equals(username) && "bupt".equals(password)) {
            voteService.clearVote();
            CommonResult.success("成功清空投票结果");
        }
        return CommonResult.failure("用户名或密码错误");
    }

    @RequestMapping(value = "/unvoted", method = RequestMethod.GET)
    public @ResponseBody  CommonResult getUnvotedExpertCount() {
        Map<String, Object> content = new HashMap<String, Object>();
        int count = voteService.getUnvotedExpertCount();
        content.put("count", count);
        return CommonResult.success("获取未投票专家人数", content);
    }

}
