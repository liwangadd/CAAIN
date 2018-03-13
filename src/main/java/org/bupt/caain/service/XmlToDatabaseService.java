package org.bupt.caain.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class XmlToDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void storeXMLtoDataBase() throws FileNotFoundException, DocumentException {
        parseDoc();
        parseExpert();
    }

    private Element getRootElement(String filePath) throws FileNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        File xmlFile = ResourceUtils.getFile(filePath);
        Document document = reader.read(xmlFile);
        return document.getRootElement();
    }

    private void parseExpert() throws FileNotFoundException, DocumentException {
        Element rootElement = getRootElement("classpath:config/kjcexpert.xml");
        List<Element> expertElements = rootElement.elements("expert");
        for (Element expertElement : expertElements) {
            String no = expertElement.elementText("no");
            String ip = expertElement.elementText("ip");
            jdbcTemplate.update("INSERT INTO expert (num, ip) VALUES (?, ?)", no, ip);
        }
    }

    private void parseDoc() throws FileNotFoundException, DocumentException {
        Element rootElement = getRootElement("classpath:config/kjcdoc.xml");
        List<Element> awardElements = rootElement.elements("award");
        for (Element awardElement : awardElements) {
            final String award = awardElement.attributeValue("name");
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator creator = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO award (award_name) VALUES (?)", new int[]{1});
                    ps.setString(1, award);
                    return ps;
                }
            };
            jdbcTemplate.update(creator, keyHolder);
            int awardId = keyHolder.getKey().intValue();
            List<Element> entryElements = awardElement.elements("entry");
            for (Element entryElement : entryElements) {
                String entry = entryElement.attributeValue("name");
                jdbcTemplate.update("INSERT INTO entry (entry_name, award_id) VALUES (?, ?)", entry, awardId);
            }
        }
    }

}
