package org.bupt.caain.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.bupt.caain.pojo.jo.VotePerExpert;
import org.bupt.caain.pojo.po.Entry;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PrintUtils {
    public String printVoteResultPerExpert(List<VotePerExpert> votesOfExpert, String filePath, String title, String expertNum) throws DocumentException {
        // 打开PDF文件
        Document document = null;
        try {
            document = openDocument(filePath);
        } catch (IOException e) {
            return "创建PDF目录失败";
        }

        printLogo(document);

        //打印奖项标题
        printTitle(document, title);

        addBlankLine(document, 2);
        Font cellFont = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", 18);
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell1 = new PdfPCell(new Paragraph("项目名称", cellFont));
        cell1.setColspan(2);
        cell1.setMinimumHeight(20);
        PdfPCell cell2 = new PdfPCell(new Paragraph("个人投票", cellFont));
        cell2.setMinimumHeight(20);
        table.addCell(cell1);
        table.addCell(cell2);

        for (VotePerExpert votePerExpert : votesOfExpert) {
            PdfPCell nameCell = new PdfPCell(new Paragraph(votePerExpert.getEntry_name(), cellFont));
            nameCell.setColspan(2);
            nameCell.setMinimumHeight(20);
            PdfPCell prizeCell = new PdfPCell(new Paragraph(votePerExpert.getEntry_prize(), cellFont));
            prizeCell.setMinimumHeight(20);
            table.addCell(nameCell);
            table.addCell(prizeCell);
        }
        document.add(table);

        addBlankLine(document, 3);
        Paragraph signParagraph = new Paragraph(expertNum + "  专家签字:", cellFont);
        signParagraph.setAlignment(Element.ALIGN_RIGHT);
        signParagraph.setIndentationRight(150);
        document.add(signParagraph);

        document.close();
        return "Done";
    }

    public String printVoteResult(List<Entry> entries, String filePath, String title) throws DocumentException {
        Document document = null;
        try {
            document = openDocument(filePath);
        } catch (IOException e) {
            return "创建PDF目录失败";
        }

        printLogo(document);

        printTitle(document, title);

        addBlankLine(document, 2);
        Font cellFont = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", 18);
        PdfPTable table = new PdfPTable(9);
        PdfPCell cell1 = new PdfPCell(new Paragraph("项目名称", cellFont));
        cell1.setColspan(5);
        cell1.setMinimumHeight(20);
        PdfPCell cell2 = new PdfPCell(new Paragraph("一等奖", cellFont));
        cell2.setMinimumHeight(20);
        PdfPCell cell3 = new PdfPCell(new Paragraph("二等奖", cellFont));
        cell2.setMinimumHeight(20);
        PdfPCell cell4 = new PdfPCell(new Paragraph("三等奖", cellFont));
        cell3.setMinimumHeight(20);
        PdfPCell cell5 = new PdfPCell(new Paragraph("获奖结果", cellFont));
        cell5.setMinimumHeight(20);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);

        for (Entry entry : entries) {
            PdfPCell nameCell = new PdfPCell(new Paragraph(entry.getEntry_name(), cellFont));
            nameCell.setColspan(5);
            nameCell.setMinimumHeight(20);
            PdfPCell level1Cell = new PdfPCell(new Paragraph(entry.getLevel1() + "", cellFont));
            level1Cell.setMinimumHeight(20);
            PdfPCell level2Cell = new PdfPCell(new Paragraph(entry.getLevel2() + "", cellFont));
            level2Cell.setMinimumHeight(20);
            PdfPCell level3Cell = new PdfPCell(new Paragraph(entry.getLevel3() + "", cellFont));
            level3Cell.setMinimumHeight(20);
            PdfPCell prizeCell = new PdfPCell(new Paragraph(entry.getEntry_prize() + "", cellFont));
            table.addCell(nameCell);
            table.addCell(level1Cell);
            table.addCell(level2Cell);
            table.addCell(level3Cell);
            table.addCell(prizeCell);
        }
        document.add(table);

        addBlankLine(document, 3);
        Paragraph signParagraph = new Paragraph("组长签字:", cellFont);
        signParagraph.setAlignment(Element.ALIGN_RIGHT);
        signParagraph.setIndentationRight(150);
        document.add(signParagraph);

        Paragraph subSignParagraph = new Paragraph("副组长签字:", cellFont);
        subSignParagraph.setAlignment(Element.ALIGN_RIGHT);
        subSignParagraph.setIndentationRight(150);
        document.add(subSignParagraph);

        document.close();
        return "Done";
    }

    private Document openDocument(String filePath) throws IOException, DocumentException {
        Document document = new Document();
        File pdfFile = new File(filePath);
        File pdfDir = pdfFile.getParentFile();
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();
        return document;
    }

    private void printLogo(Document document) {
        //打印log
        try {
            Image image = Image.getInstance(ResourceUtils.getFile("classpath:logo.png").getAbsolutePath());
            image.setAlignment(Image.ALIGN_CENTER);
            document.add(image);
        } catch (Exception e) {
            System.out.println("人工智能协会Logo不存在");
        }
    }

    private void printTitle(Document document, String title) throws DocumentException {
        Font chapterFont = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", 28, Font.BOLD);
        Chunk chunk = new Chunk("吴文俊人工智能 -- " + title, chapterFont);
        Paragraph titleParagraph = new Paragraph(chunk);
        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        addBlankLine(document, 2);
        document.add(titleParagraph);
    }

    private void addBlankLine(Document document, int lineNum) throws DocumentException {
        while (lineNum-- > 0) {
            document.add(new Paragraph(" "));
        }
    }
}
