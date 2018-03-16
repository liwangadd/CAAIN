package org.bupt.caain.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileOutputStream;

public class StartTutorial {

    @Test
    public void createPDF() throws Exception {
        // 打开PDF文件
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("H:\\caain_save\\iText.pdf"));
        document.open();

        //打印log
        Image image = Image.getInstance(ResourceUtils.getFile("classpath:logo.png").getAbsolutePath());
        image.setAlignment(Image.ALIGN_CENTER);
        document.add(image);

        //打印奖项标题
        Font chapterFont = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", 28, Font.BOLD);
        Chunk chunk = new Chunk("自然科学奖", chapterFont);
        Paragraph titleParagraph = new Paragraph(chunk);
        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(titleParagraph);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        Font cellFont = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H");
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell1 = new PdfPCell(new Paragraph("项目名称", cellFont));
        cell1.setColspan(2);
        cell1.setFixedHeight(20);
        PdfPCell cell2 = new PdfPCell(new Paragraph("个人投票", cellFont));
        cell2.setFixedHeight(20);
        table.addCell(cell1);
        table.addCell(cell2);
        document.add(table);

        Paragraph signParagraph = new Paragraph("专家签字:", cellFont);
        signParagraph.setAlignment(Element.ALIGN_RIGHT);
        signParagraph.setIndentationRight(150);
        document.add(signParagraph);

        document.close();
    }

}
