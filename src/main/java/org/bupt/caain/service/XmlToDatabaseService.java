package org.bupt.caain.service;

import org.bupt.caain.model.AttachModel;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.model.EntryModel;
import org.bupt.caain.model.ExpertModel;
import org.bupt.caain.pojo.po.Attach;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.pojo.po.Expert;
import org.bupt.caain.utils.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

@Service
public class XmlToDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(XmlToDatabaseService.class);

    private static final String SEPARATOR = File.separator;

    private static final String EXPERT_NUM = "no";
    private static final String EXPERT_IP = "ip";

    private final AwardModel awardModel;

    private final ExpertModel expertModel;

    private final EntryModel entryModel;

    private final AttachModel attachModel;

    @Value("${doc-dir}")
    private String docPath;

    @Autowired
    public XmlToDatabaseService(AwardModel awardModel, ExpertModel expertModel, EntryModel entryModel, AttachModel attachModel) {
        this.awardModel = awardModel;
        this.expertModel = expertModel;
        this.entryModel = entryModel;
        this.attachModel = attachModel;
    }

    @PostConstruct
    public void storeXMLtoDataBase() throws FileNotFoundException, DocumentException {
        parseDoc();
        parseExpert();
        log.warn("数据库初始化成功");
    }

    private Element getRootElement(String filePath) throws FileNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        File xmlFile = ResourceUtils.getFile(filePath);
        Document document = reader.read(xmlFile);
        return document.getRootElement();
    }

    private void parseExpert() throws FileNotFoundException, DocumentException {
        Element rootElement = getRootElement("classpath:config/kjcexpert.xml");
        Iterator expertIterator = rootElement.elementIterator("expert");
        while (expertIterator.hasNext()) {
            Element expertElement = (Element) expertIterator.next();
            String no = expertElement.elementText(EXPERT_NUM);
            String ip = expertElement.elementText(EXPERT_IP);
            Expert expert = new Expert(no, ip);
            expertModel.add(expert);
        }
    }

    private void parseDoc() throws FileNotFoundException {
//        Element rootElement = getRootElement("classpath:config/kjcdoc.xml");
        File caainDir = new File(docPath);
        if (!caainDir.exists() || caainDir.isFile()) {
            log.error("奖项文件夹不存在:" + docPath);
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
        Award award = new Award(awardDir.getName().replaceAll("^\\d+-", ""));
        award.setVoted(false);
        int awardId = awardModel.addAndGetId(award);

        // 获取奖项下的所有申请项目
        File[] entryDirs = awardDir.listFiles(File::isDirectory);
        // 插入项目信息
        if (entryDirs != null && entryDirs.length > 0) {
            for (File entryDir : entryDirs) {
                insertEntry(awardId, entryDir, awardPath + SEPARATOR + entryDir.getName());
            }
        }
    }

    //插入项目信息
    private void insertEntry(int awardId, File entryDir, String entryPath) {
        // 插入项目信息并返回奖项ID
        String entryName = entryDir.getName();
        Entry entry = new Entry(entryName.replaceAll("^\\d+-", ""), awardId);

        // 更新项目申请书名称和申请书地址
        File[] applicationFiles = entryDir.listFiles(pathname -> pathname.getName().endsWith(".pdf"));
        if (applicationFiles != null && applicationFiles.length > 0) {
            File applicationFile = applicationFiles[0];
            String applicationPath = entryPath + SEPARATOR + applicationFile.getName();
            entry.setEntry_application(FileUtils.getFileNameNoExtension(applicationFile.getName()));
            entry.setApplication_path(applicationPath);
        }
        int entryId = entryModel.addAndGetId(entry);

        // 持久化申请项目的所有附件
        File attachDir = new File(entryDir.getAbsolutePath() + SEPARATOR + "附件");
        File[] attaches = attachDir.listFiles();
//        File[] attachFiles = attachDir.listFiles(pathname -> pathname.getName().endsWith(".pdf"));
        if (attaches != null && attaches.length > 0) {
            for (File attachFile : attaches) {
                insertAttach(entryId, attachFile, entryPath + SEPARATOR + "附件", -1);
            }
        }
    }

    // 持久化附件信息
    private void insertAttach(int entryId, File attachFile, String attachDirPath, int parentId) {
        if (attachFile.isFile()) {
            //如果是文件，直接将附件文件插入到数据库
            String attachName = FileUtils.getFileNameNoExtension(attachFile);
            String attachPath = attachDirPath + SEPARATOR + attachFile.getName();
//            System.out.println(attachPath);
            Attach attach = new Attach(attachName, attachPath, entryId);
            attach.setIs_dir(false);
            attach.setParent_id(parentId);
            attachModel.addAndGetId(attach);
        } else {
            Attach attach = new Attach(attachFile.getName(), attachDirPath + SEPARATOR + attachFile.getName(), entryId);
            attach.setIs_dir(true);
            attach.setParent_id(parentId);
            int attachId = attachModel.addAndGetId(attach);

            //如果是文件夹，递归遍历该文件夹
            File[] attaches = attachFile.listFiles();
            if (attaches != null && attaches.length > 0) {
                for (File childAttach : attaches) {
                    insertAttach(entryId, childAttach, attach.getAttach_path(), attachId);
                }
            }
        }
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
