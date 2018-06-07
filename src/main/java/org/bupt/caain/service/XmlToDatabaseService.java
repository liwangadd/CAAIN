package org.bupt.caain.service;

import org.bupt.caain.utils.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class XmlToDatabaseService {

    private Logger logger = LoggerFactory.getLogger(XmlToDatabaseService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${doc-dir}")
    private String docPath;

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
//        Element rootElement = getRootElement("classpath:config/kjcdoc.xml");
        File caainDir = new File(docPath);
        if (!caainDir.exists() || caainDir.isFile()) {
            logger.error("奖项文件夹不存在");
            throw new FileNotFoundException("奖项文件夹不存在");
        }
        File[] awardDirs = caainDir.listFiles(File::isDirectory);
        if (awardDirs != null && awardDirs.length > 0) {
            for (File awardDir : awardDirs) {
                insertAward(awardDir, awardDir.getName());
            }
        }
    }

    // 插入奖项信息
    private void insertAward(File awardDir, String awardPath) {
        //插入奖项信息并返回奖项ID
        String awardName = awardDir.getName();
        KeyHolder awardKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO award (award_name) VALUES (?)", new int[]{1});
            ps.setString(1, awardName);
            return ps;
        }, awardKeyHolder);
        int awardId = awardKeyHolder.getKey().intValue();

        // 获取奖项下的所有申请项目
        File[] entryDirs = awardDir.listFiles(File::isDirectory);
        // 插入项目信息
        if (entryDirs != null && entryDirs.length > 0) {
            for (File entryDir : entryDirs) {
                insertEntry(awardId, entryDir, awardPath + "/" + entryDir.getName());
            }
        }
    }

    //插入项目信息
    private void insertEntry(int awardId, File entryDir, String entryPath) {
        // 插入项目信息并返回奖项ID
        String entryName = entryDir.getName();
        KeyHolder entryKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO entry (entry_name, award_id) VALUES (?, ?)", new int[]{1, 2});
            statement.setString(1, entryName);
            statement.setInt(2, awardId);
            return statement;
        }, entryKeyHolder);
        int entryId = Integer.parseInt(entryKeyHolder.getKeys().get("ID").toString());

        // 更新项目申请书名称和申请书地址
        File[] applicationFiles = entryDir.listFiles(pathname -> pathname.getName().endsWith(".pdf"));
        if (applicationFiles != null && applicationFiles.length > 0) {
            File applicationFile = applicationFiles[0];
            String applicationPath = entryPath + "/" + applicationFile.getName();
            jdbcTemplate.update("UPDATE entry SET entry_application = ?, application_path = ? WHERE id = ?",
                    FileUtils.getFileNameNoExtension(applicationFile.getName()),
                    applicationPath,
                    entryId);
        }

        // 持久化申请项目的所有附件
        File attachDir = new File(entryDir.getAbsolutePath() + "/附件");
        File[] attachFiles = attachDir.listFiles(pathname -> pathname.getName().endsWith(".pdf"));
        if (attachFiles.length > 0) {
            for (File attachFile : attachFiles) {
                insertAttach(entryId, attachFile, entryPath + "/附件");
            }
        }
    }

    // 持久化附件信息
    private void insertAttach(int entryId, File attachFile, String attachDirPath) {
        String attachName = FileUtils.getFileNameNoExtension(attachFile);
        String attachPath = attachDirPath + "/" + attachFile.getName();
        jdbcTemplate.update("INSERT INTO attach (attach_name, attach_path, entry_id) VALUES (?, ?, ?)", attachName, attachPath, entryId);
    }

//        List<Element> awardElements = rootElement.elements("award");
//        for (Element awardElement : awardElements) {
//            final String award = awardElement.attributeValue("name");
//            KeyHolder awardKeyHolder = new GeneratedKeyHolder();
//            PreparedStatementCreator creator = conn -> {
//                PreparedStatement ps = conn.prepareStatement("INSERT INTO award (award_name) VALUES (?)", new int[]{1});
//                ps.setString(1, award);
//                return ps;
//            };
//            jdbcTemplate.update(creator, awardKeyHolder);
//            final int awardId = awardKeyHolder.getKey().intValue();
//            List<Element> entryElements = awardElement.elements("entry");
//            for (Element entryElement : entryElements) {
//                final String entry = entryElement.attributeValue("name");
//                KeyHolder entryKeyHolder = new GeneratedKeyHolder();
//                jdbcTemplate.update(connection -> {
//                    PreparedStatement statement = connection.prepareStatement("INSERT INTO entry (entry_name, award_id) VALUES (?, ?)", new int[]{1, 2});
//                    statement.setString(1, entry);
//                    statement.setInt(2, awardId);
//                    return statement;
//                }, entryKeyHolder);
//                int entryId = Integer.parseInt(entryKeyHolder.getKeys().get("ID").toString());
//                List<Element> attachElements = entryElement.elements("attach");
//                for (Element attachElement : attachElements) {
//                    String attachName = attachElement.getText();
//                    jdbcTemplate.update("INSERT INTO attach (attach_name, entry_id) VALUES (?, ?)", attachName, entryId);
//                }
//            }
//        }

}
