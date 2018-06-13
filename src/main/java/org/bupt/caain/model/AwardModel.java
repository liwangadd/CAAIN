package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Award;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class AwardModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addAndGetId(Award award) {
        KeyHolder awardKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO award (award_name, voted) VALUES (?, ?)", new int[]{1, 2});
            ps.setString(1, award.getAward_name());
            ps.setBoolean(2, award.isVoted());
            return ps;
        }, awardKeyHolder);
        int awardId = (Integer)awardKeyHolder.getKeys().get("id");
        return awardId;
    }

    /**
     * 获取所有评奖奖项
     *
     * @return 奖项列表
     */
    public List<Award> queryAll() {
        return jdbcTemplate.query("SELECT id, award_name, voted FROM award", new BeanPropertyRowMapper<Award>(Award.class));
    }

    /**
     * 根据评奖奖项id获取奖项信息
     *
     * @param id 奖项id
     * @return 奖项信息
     */
    public Award queryById(int id) {
        List<Award> awards = jdbcTemplate.query("SELECT id, award_name, voted FROM award WHERE id = ?", new BeanPropertyRowMapper<>(Award.class), id);
        if (awards != null && awards.size() > 0) {
            return awards.get(0);
        } else {
            return null;
        }
    }

    public List<Award> queryVoteAwards() {
        List<Award> awards = jdbcTemplate.query("SELECT id, award_name, voted FROM award WHERE voted = 1", new BeanPropertyRowMapper<>(Award.class));
        if(awards!=null&&awards.size()>0){
            return awards;
        }else{
            return null;
        }
    }

    public int update(Award award){
        return jdbcTemplate.update("UPDATE award SET award_name = ?, voted = ? WHERE id = ?", award.getAward_name(), award.isVoted(), award.getId());
    }
}
