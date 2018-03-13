package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.EntryExpert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntryExpertModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(EntryExpert entryExpert){
        jdbcTemplate.update("INSERT INTO entry_expert(entry_id, expert_id, level1, level2, level3) VALUE (?, ?, ?, ?, ?)",
                entryExpert.getEntry_id(), entryExpert.getExpert_id(), entryExpert.getLevel1(), entryExpert.getLevel2(),
                entryExpert.getLevel3());
    }

}
