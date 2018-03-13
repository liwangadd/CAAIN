package org.bupt.caain.controller;

import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.EntryExpert;
import org.bupt.caain.pojo.vo.VoteVo;
import org.bupt.caain.service.HomeService;
import org.bupt.caain.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "vote", method = RequestMethod.GET)
    public String getVotePage(){
        return "vote";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody List<Award> getAwards(){
        return homeService.getAwards();
    }

    @RequestMapping(value = "entries/{award_id}", method = RequestMethod.GET)
    public @ResponseBody List<VoteVo> getVoteForType(@PathVariable("award_id")int awardId){
        return voteService.getEntriesForType(awardId);
    }

    @RequestMapping(value = "vote", method = RequestMethod.POST)
    public void votePerExpert(@RequestBody EntryExpert entryExpert){

    }

}
