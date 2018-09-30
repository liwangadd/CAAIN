package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.vo.VoteEntryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class EntryModel {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(EntryModel.class);

    @Value("${first_threshold}")
    private int firstThreshold;

    @Value("${second_threshold}")
    private int secondThreshold;

    @Autowired
    public EntryModel(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 添加申报项目并返回主键内容
     *
     * @param entry 项目信息
     * @return 主键值
     */
    public int addAndGetId(Entry entry) {
        KeyHolder entryKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO entry (entry_name, entry_prize, entry_application, application_path, level1, level2, level3, award_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new int[]{1, 2, 3, 4, 5, 6, 7, 8});
            statement.setString(1, entry.getEntry_name());
            statement.setString(2, entry.getEntry_prize());
            statement.setString(3, entry.getEntry_application());
            statement.setString(4, entry.getApplication_path());
            statement.setInt(5, entry.getLevel1() == null ? 0 : entry.getLevel1());
            statement.setInt(6, entry.getLevel2() == null ? 0 : entry.getLevel2());
            statement.setInt(7, entry.getLevel3() == null ? 0 : entry.getLevel3());
            statement.setInt(8, entry.getAward_id());
            return statement;
        }, entryKeyHolder);
        int entryId = Integer.parseInt(entryKeyHolder.getKeys().get("ID").toString());
        return entryId;
    }

    /**
     * 根据award_id字段获取参赛作品
     *
     * @param awardId 奖项
     * @return
     */
    public List<Entry> queryEntriesByAwardId(int awardId) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, entry_name, entry_prize, entry_application, application_path, level1, level2, level3, award_id " +
                        "FROM entry WHERE award_id = ?",
                new BeanPropertyRowMapper<>(Entry.class), awardId);
        return entries;
    }

    /**
     * 根据投票结果生成Prize字段
     *
     * @param awardId
     */
    public void buildPrizeField(int awardId) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, level1, level2, level3 FROM entry WHERE award_id = ?", new BeanPropertyRowMapper<>(Entry.class),
                awardId);
        log.warn(entries.toString());
        for (Entry entry : entries) {
            if (entry.getLevel1() >= firstThreshold) {
                entry.setEntry_prize("一等奖");
            } else if (entry.getLevel1() + entry.getLevel2() >= secondThreshold) {
                entry.setEntry_prize("二等奖");
            } else {
                entry.setEntry_prize("三等奖");
            }
            log.warn(entry.getEntry_prize() + " " + entry.getId());
            jdbcTemplate.update("UPDATE entry SET entry_prize = ? WHERE id = ?", entry.getEntry_prize(), entry.getId());
        }
    }

    /**
     * 根据主键查询申报项目
     *
     * @param id 项目ID
     * @return 申报项目信息
     */
    public Entry queryById(int id) {
        List<Entry> entries = jdbcTemplate.query("SELECT id, entry_name, entry_prize, entry_application, application_path, level1, level2, level3, award_id " +
                        "FROM entry WHERE id = ?",
                new BeanPropertyRowMapper<>(Entry.class), id);
        if (null != entries && entries.size() > 0) {
            return entries.get(0);
        }
        return null;
    }

    /**
     * 更新申报项目的投票信息
     *
     * @param entry 项目投票结果
     */
    public void updateLevelById(Entry entry) {
        jdbcTemplate.update("UPDATE entry SET level1 = ?, level2 = ?, level3 = ? WHERE id = ?", entry.getLevel1(),
                entry.getLevel2(), entry.getLevel3(), entry.getId());
    }

    /**
     * 重置投票结果
     */
    public void resetAll() {
        jdbcTemplate.update("UPDATE entry SET entry_prize='', level1 = 0, level2 = 0, level3 = 0 WHERE award_id in (" +
                "SELECT id FROM award WHERE voted = 1)");
    }

    /**
     * 重置申报指定奖项项目的投票结果
     *
     * @param awardId 奖项ID
     */
    public void resetByAwardId(int awardId) {
        jdbcTemplate.update("UPDATE entry SET entry_prize='', level1 = 0, level2 = 0, level3 = 0 WHERE award_id = ?", awardId);
    }

    public List<Integer> queryIdsByAwardId(int awardId) {
        List<Integer> entryIds = jdbcTemplate.queryForList("SELECT id FROM entry WHERE award_id = ?", new Object[]{awardId}, Integer.class);
        log.warn(entryIds.toString());
        return entryIds;
    }

    public List<VoteEntryVo> queryEntriesWithPrize(int awardId, int expertId) {
        return jdbcTemplate.query("SELECT e.id, e.entry_name, e.award_id, ee.level1, ee.level2, ee.level3 FROM entry as e LEFT JOIN " +
                        "(SELECT entry_id, level1, level2, level3 FROM entry_expert WHERE expert_id = ?) as ee ON e.id = ee.entry_id WHERE e.award_id = ?",
                new BeanPropertyRowMapper<>(VoteEntryVo.class), awardId, expertId);
    }

    public List<Entry> queryVotedEntries() {
        return jdbcTemplate.query("SELECT e.id, e.entry_name, e.entry_prize, e.entry_application, e.application_path, e.level1, e.level2, e.level3, e.award_id" +
                " FROM entry e left join award a on e.award_id = a.id where a.voted = 1", new BeanPropertyRowMapper<>(Entry.class));
    }
}
