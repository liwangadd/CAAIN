package org.bupt.caain.controller;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.pojo.vo.HomeTreeAwardVO;
import org.bupt.caain.pojo.vo.VoteVo;
import org.bupt.caain.service.AdminService;
import org.bupt.caain.service.HomeService;
import org.bupt.caain.service.PrintService;
import org.bupt.caain.service.VoteService;
import org.bupt.caain.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "vote", method = RequestMethod.GET)
    public String getVotePage() {
        return "vote";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getAwards() {
        List<Award> awards = homeService.getVoteAwards();
        if (null != awards && awards.size() > 0) {
            return CommonResult.success("获取全部奖项", awards);
        } else {
            return CommonResult.failure("投票还未开始");
        }
    }

    @RequestMapping(value = "expert", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getExpertByIp(HttpServletRequest request) {
        String ip = getIpAddr(request);
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

    @RequestMapping(value = "vote", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult votePerExpert(@RequestBody List<VotePerExpert> votesOfExpert) {
        System.out.println(votesOfExpert);
        if(votesOfExpert.size()==0){
            return CommonResult.failure("投票信息不完整");
        }
        boolean isSuccess = voteService.votePerExpert(votesOfExpert);
        if (isSuccess) {
            try {
                adminService.printVotesPerExpert(votesOfExpert);
            } catch (DocumentException e) {
                System.out.println("PDF文件生成失败");
            }
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


    @RequestMapping(value = "/unvoted", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getUnvotedExpertCount() {
        Map<String, Object> content = new HashMap<String, Object>();
        int count = voteService.getUnvotedExpertCount();
        content.put("count", count);
        return CommonResult.success("获取未投票专家人数", content);
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
