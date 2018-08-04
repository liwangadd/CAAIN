package org.bupt.caain.controller;

import org.bupt.caain.pojo.vo.HomeTreeAwardVO;
import org.bupt.caain.service.HomeService;
import org.bupt.caain.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @GetMapping(value = "entries")
    public @ResponseBody
    CommonResult getEntriesTree() {
        List<HomeTreeAwardVO> entriesTree = homeService.getEntriesTree();
        return CommonResult.success("获取成功", entriesTree);
    }

}
