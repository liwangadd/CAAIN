package org.bupt.caain.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExpertModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取expert表的总行数
     *
     * @return
     */
    public int queryCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM expert", Integer.class);
    }

    /**
     * 重置voted字段
     */
    public void resetVote() {
        jdbcTemplate.update("UPDATE expert SET voted = 0");
    }

    public void updateById(int id) {
        jdbcTemplate.update("UPDATE expert SET voted = ? WHERE id = ?", 1, id);
    }

    public int queryVotedCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM expert WHERE voted = 1", Integer.class);
    }

}
