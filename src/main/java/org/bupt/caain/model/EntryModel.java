package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        List<Entry> entries = jdbcTemplate.query("SELECT id, entry_name, entry_prize, entry_application, application_path, level1, level2, level3, award_id FROM entry WHERE award_id = ?",
                new BeanPropertyRowMapper<>(Entry.class), awardId);
        return entries;
    }

    /**
     * 根据投票结果生成Prize字段
     *
     * @param awardId
     */
    public void buildPrizeField(int awardId) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, level1, level2, level3 FROM entry WHERE award_id = ?", new BeanPropertyRowMapper<Entry>(Entry.class),
                awardId);
        for (Entry entry : entries) {
            if (entry.getLevel1() >= 8) {
                entry.setEntry_prize("一等奖");
            } else if (entry.getLevel1() + entry.getLevel2() >= 6) {
                entry.setEntry_name("二等奖");
            } else {
                entry.setEntry_prize("三等奖");
            }
            jdbcTemplate.update("UPDATE entry SET entry_prize = ? WHERE id = ?", entry.getEntry_prize(), entry.getId());
        }
    }

    public Entry queryById(int id) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, entry_name, entry_prize, entry_application, application_path, level1, level2, level3, award_id FROM entry WHERE id = ?",
                new BeanPropertyRowMapper<>(Entry.class), id);
        if (null != entries && entries.size() > 0) {
            return entries.get(0);
        }
        return null;
    }

    public void updateLevelById(Entry entry) {
        jdbcTemplate.update("UPDATE entry SET level1 = ?, level2 = ?, level3 = ? WHERE id = ?", entry.getLevel1(),
                entry.getLevel2(), entry.getLevel3(), entry.getId());
    }

    public void resetAll() {
        jdbcTemplate.update("UPDATE entry SET entry_prize='', level1 = 0, level2 = 0, level3 = 0");
    }

    public void deleteByAwardId(int awardId) {
        jdbcTemplate.update("UPDATE entry SET entry_prize='', level1 = 0, level2 = 0, level3 = 0 WHERE award_id = ?", awardId);
    }
}
