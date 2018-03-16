package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Expert queryById(int expertId) {
        List<Expert> experts = jdbcTemplate.query("SELECT id, num, ip, voted FROM expert WHERE id = ?",
                new BeanPropertyRowMapper<>(Expert.class), expertId);
        if (null != experts && experts.size() > 0) {
            return experts.get(0);
        }
        return null;
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

    public Expert queryByIp(String ip) {
        List<Expert> experts = jdbcTemplate.query("SELECT id, num, ip, voted FROM expert WHERE ip = ?", new BeanPropertyRowMapper<Expert>(Expert.class), ip);
        if (experts != null && experts.size() > 0) {
            return experts.get(0);
        }
        return null;
    }

}
