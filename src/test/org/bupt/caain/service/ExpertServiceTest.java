package org.bupt.caain.service;

import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.Expert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration("classpath:spring/spring-data.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExpertServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void textInsert() {
        List<Expert> experts = jdbcTemplate.query("SELECT * FROM expert", new BeanPropertyRowMapper<Expert>(Expert.class));
        for (Expert expert : experts) {
            System.out.println(expert);
        }

        List<Entry> entries = jdbcTemplate.query("SELECT * FROM entry", new BeanPropertyRowMapper<Entry>(Entry.class));
        for(Entry entry: entries){
            System.out.println(entry);
        }

        List<Award> awards = jdbcTemplate.query("SELECT * FROM award", new BeanPropertyRowMapper<Award>(Award.class));
        for(Award award:awards){
            System.out.println(award);
        }
    }

    public void sourceRead(){
        ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
    }

}
