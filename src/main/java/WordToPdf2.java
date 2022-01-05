// https://newbedev.com/java-using-apache-poi-how-to-convert-ms-word-file-to-pdf

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class WordToPdf2 {

    public static void test1() {

        String k = null;
        OutputStream fileForPdf = null;
        try {
            String fileName = "d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx";
            String fileNamePdf = "d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf";
            //Below Code is for .doc file
            if(fileName.endsWith(".doc"))
            {
                // Не сработало
                HWPFDocument doc = new HWPFDocument(new FileInputStream(
                        fileName));
                WordExtractor we=new WordExtractor(doc);
                k = we.getText();

                fileForPdf = new FileOutputStream(new File(fileNamePdf));
                we.close();
            }

            //Below Code for

            else if(fileName.endsWith(".docx"))
            {
                XWPFDocument docx = new XWPFDocument(new FileInputStream(
                        fileName));
                // using XWPFWordExtractor Class
                XWPFWordExtractor we = new XWPFWordExtractor(docx);
                k = we.getText();

                fileForPdf = new FileOutputStream(new File(fileNamePdf));
                we.close();
            }

            Document document = new Document();
            PdfWriter.getInstance(document, fileForPdf);

            document.open();

            document.add(new Paragraph(k));

            document.close();
            fileForPdf.close();

            // Сработало, но файл - говно

            System.out.println("Document testing completed");
        } catch (Exception e) {
            System.out.println("Exception during test");
            e.printStackTrace();
        }
    }

    public void test2(){
        String inputFile = "d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx";
        String outputFile = "d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf";
        System.out.println("inputFile:" + inputFile + ",outputFile:"+ outputFile);
        FileInputStream in= null;
        try {
            in = new FileInputStream(inputFile);
            XWPFDocument document=new XWPFDocument(in);
            File outFile=new File(outputFile);
            OutputStream out=new FileOutputStream(outFile);
            PdfOptions options=null;
            PdfConverter.getInstance().convert(document,out,options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
