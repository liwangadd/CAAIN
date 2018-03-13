package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Award;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AwardModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Award> queryAll() {
        return jdbcTemplate.query("SELECT id, award_name FROM award", new BeanPropertyRowMapper<Award>(Award.class));
    }

}
