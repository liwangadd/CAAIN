package org.bupt.caain.model;

import org.aspectj.weaver.ast.Expr;
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

    public void add(Expert expert) {
        jdbcTemplate.update("INSERT INTO expert (num, ip, voted) VALUES (?, ?, ?)", expert.getNum(), expert.getIp(), expert.getVoted());
    }

    /**
     * 获取专家总人数
     *
     * @return 专家总人数
     */
    public int queryCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM expert", Integer.class);
    }

    /**
     * 获取已投票专家人数
     *
     * @return 投票专家人数
     */
    public int queryVotedCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM expert WHERE voted = 1", Integer.class);
    }

    /**
     * 根据id获取专家信息
     *
     * @param expertId 专家id
     * @return 专家信息
     */
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
     * 更新所有专家状态为未投票
     */
    public void resetVote() {
        jdbcTemplate.update("UPDATE expert SET voted = 0");
    }

    /**
     * 更新专家投票状态
     *
     * @param voted 投票状态
     * @param id    专家id
     */
    public void updateById(boolean voted, int id) {
        if (voted) {
            jdbcTemplate.update("UPDATE expert SET voted = ? WHERE id = ?", 1, id);
        } else {
            jdbcTemplate.update("UPDATE expert SET voted = ? WHERE id = ?", 0, id);
        }
    }

    /**
     * 根据id查询专家信息
     *
     * @param ip 专家ip地址
     * @return 专家信息
     */
    public Expert queryByIp(String ip) {
        List<Expert> experts = jdbcTemplate.query("SELECT id, num, ip, voted FROM expert WHERE ip = ?", new BeanPropertyRowMapper<>(Expert.class), ip);
        if (experts != null && experts.size() > 0) {
            return experts.get(0);
        }
        return null;
    }

}
