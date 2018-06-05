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

    /**
     * 获取所有评奖奖项
     * @return 奖项列表
     */
    public List<Award> queryAll() {
        return jdbcTemplate.query("SELECT id, award_name FROM award", new BeanPropertyRowMapper<Award>(Award.class));
    }

    /**
     * 根据评奖奖项id获取奖项信息
     * @param id 奖项id
     * @return 奖项信息
     */
    public Award queryById(int id) {
        List<Award> awards = jdbcTemplate.query("SELECT id, award_name FROM award WHERE id = ?", new BeanPropertyRowMapper<>(Award.class), id);
        if (awards != null && awards.size() > 0) {
            return awards.get(0);
        } else {
            return null;
        }
    }

}
