package org.bupt.caain.model;

import org.bupt.caain.pojo.po.Attach;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttachModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加附件
     *
     * @param attach 附件信息
     */
    public void add(Attach attach) {
        jdbcTemplate.update("INSERT INTO attach (attach_name, attach_path, entry_id) VALUES (?, ?, ?)",
                attach.getAttach_name(), attach.getAttach_path(), attach.getEntry_id());
    }

    /**
     * 根据申请作品id获取作品中的所有附件
     *
     * @param entryId 作品id
     * @return 附件列表
     */
    public List<Attach> queryByEntryId(int entryId) {
        return jdbcTemplate.query("SELECT id, attach_name, attach_path, entry_id FROM attach WHERE entry_id = ?", new BeanPropertyRowMapper<>(Attach.class),
                entryId);
    }

    /**
     * 根据附件ID查询
     *
     * @param id 附件id
     * @return 附件信息
     */
    public Attach queryById(int id) {
        List<Attach> attaches = jdbcTemplate.query("SELECT id, attach_name, attach_path, entry_id FROM attach WHERE id = ?", new BeanPropertyRowMapper<>(Attach.class), id);
        if (attaches != null && attaches.size() > 0) {
            return attaches.get(0);
        } else {
            return null;
        }
    }

}
