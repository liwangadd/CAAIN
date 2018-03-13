package org.bupt.caain.model;

import com.sun.java.accessibility.util.EventID;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EntryModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据award_id字段获取参赛作品
     *
     * @param awardId 奖项
     * @return
     */
    public List<Entry> queryEntriesByAwardId(int awardId) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, entry_name, entry_prize FROM entry WHERE award_id = ?", new RowMapper<Entry>() {
            public Entry mapRow(ResultSet resultSet, int i) throws SQLException {
                Entry entry = new Entry();
                entry.setId(resultSet.getInt("id"));
                entry.setPrize(resultSet.getString("entry_prize"));
                entry.setEntry_name(resultSet.getString("entry_name"));
                return entry;
            }
        }, awardId);
        return entries;
    }

    /**
     * 根据投票结果生成Prize字段
     *
     * @param awardId
     */
    public void buildPrizeField(int awardId) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, level1, level2, level3 FROM entry WHERE award_id = ?", new BeanPropertyRowMapper<Entry>(),
                awardId);
        for (Entry entry : entries) {
            if (entry.getLevel1() >= 8) {
                entry.setPrize("一等奖");
            } else if (entry.getLevel1() + entry.getLevel2() >= 6) {
                entry.setPrize("二等奖");
            } else {
                entry.setPrize("三等奖");
            }
            jdbcTemplate.update("UPDATE entry SET prize = ?", entry.getPrize());
        }
    }

    public Entry queryById(int id) {
        return jdbcTemplate.queryForObject("SELECT level1, level2, level3 FROM entry FROM entry WHERE id = ?", Entry.class, id);
    }

    public void updateLevelById(Entry entry) {
        jdbcTemplate.update("UPDATE entry SET level1 = ?, level2 = ?, level3 = ? WHERE id = ?", entry.getLevel1(),
                entry.getLevel2(), entry.getLevel3(), entry.getId());
    }

}
