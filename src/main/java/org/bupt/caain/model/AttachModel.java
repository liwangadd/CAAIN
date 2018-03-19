package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Attach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttachModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Attach> queryByEntryId(int entryId){
        return jdbcTemplate.query("SELECT id, attach_name, entry_id FROM attach WHERE entry_id = ?", new BeanPropertyRowMapper<Attach>(Attach.class),
                entryId);
    }

}
