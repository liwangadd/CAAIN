package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Attach;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class AttachModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;
//    public void add(Attach attach) {
//        jdbcTemplate.update("INSERT INTO attach (attach_name, attach_path, entry_id, is_dir, parent_id) VALUES (?, ?, ?, ?, ?)",
//                attach.getAttach_name(), attach.getAttach_path(), attach.getEntry_id());
//    }

    public int addAndGetId(Attach attach) {
        KeyHolder awardKeyHolder = new GeneratedKeyHolder();
        System.out.println(attach);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO attach (attach_name, attach_path, " +
                            "entry_id, is_dir, parent_id) VALUES (?, ?, ?, ?, ?)",
                    new int[]{1, 2});
            ps.setString(1, attach.getAttach_name());
            ps.setString(2, attach.getAttach_path());
            ps.setInt(3, attach.getEntry_id());
            ps.setBoolean(4, attach.isIs_dir());
            ps.setInt(5, attach.getParent_id());
            return ps;
        }, awardKeyHolder);
        int attachId = (Integer) awardKeyHolder.getKeys().get("id");
        return attachId;
    }

    /**
     * 根据申请作品id获取作品中的所有附件
     *
     * @param entryId 作品id
     * @return 附件列表
     */
    public List<Attach> queryByEntryId(int entryId) {
        return jdbcTemplate.query("SELECT id, attach_name, attach_path, entry_id FROM attach WHERE entry_id = ?",
                new BeanPropertyRowMapper<>(Attach.class),
                entryId);
    }

    public List<Attach> queryByEntryIdAndParentId(int entryId, int parentId) {
        return jdbcTemplate.query("SELECT id, attach_name, attach_path, entry_id, is_dir, parent_id FROM attach " +
                "WHERE entry_id = ? AND parent_id = ?", new BeanPropertyRowMapper<>(Attach.class), entryId, parentId);
    }

    /**
     * 根据附件ID查询
     *
     * @param id 附件id
     * @return 附件信息
     */
    public Attach queryById(int id) {
        List<Attach> attaches = jdbcTemplate.query("SELECT id, attach_name, attach_path, entry_id, is_dir, parent_id " +
                "FROM attach WHERE id = ?", new BeanPropertyRowMapper<>(Attach.class), id);
        if (attaches != null && attaches.size() > 0) {
            return attaches.get(0);
        } else {
            return null;
        }
    }

}
