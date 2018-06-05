package org.bupt.caain.controller;

import org.bupt.caain.pojo.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
public class ViewDataController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/entry")
    public List<Entry> viewEntry() {
        return jdbcTemplate.query("SELECT * FROM entry", new BeanPropertyRowMapper<Entry>(Entry.class));
    }

    @RequestMapping("/award")
    public List<Award> viewAward() {
        return jdbcTemplate.query("SELECT * FROM award", new BeanPropertyRowMapper<Award>(Award.class));
    }

    @RequestMapping("/attach")
    public List<Attach> viewAttach() {
        return jdbcTemplate.query("SELECT * FROM attach", new BeanPropertyRowMapper<Attach>(Attach.class));
    }

    @RequestMapping("/expert")
    public List<Expert> viewExpert() {
        return jdbcTemplate.query("SELECT * FROM expert", new BeanPropertyRowMapper<Expert>(Expert.class));
    }

    @RequestMapping("/entry_expert")
    public List<EntryExpert> viewEntryExpert() {
        return jdbcTemplate.query("SELECT * FROM entry_expert", new BeanPropertyRowMapper<EntryExpert>(EntryExpert.class));
    }

}
