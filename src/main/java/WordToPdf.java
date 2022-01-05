//import com.spire.doc.*;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class WordToPdf {

    // https://habr.com/ru/sandbox/142728/
    public void test1() {
        //Загрузить образец документа Word
        // Здесь валится
        com.spire.doc.Document document = new Document();
        document.loadFromFile("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");


        //Сохранить как PDF
        document.saveToFile("D/TEMP/Файл.pdf", FileFormat.PDF);
    }

// https://www.youtube.com/watch?v=rFDwT1kRpLk&ab_channel=ChargeAhead

    public void test2() {

        try {
            FileInputStream docFile = new FileInputStream(new File("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx"));
            XWPFDocument doc = new XWPFDocument(docFile);
            PdfOptions pdfOptions = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File("d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf"));
            // не работает
            PdfConverter.getInstance().convert(doc, out, pdfOptions);
            doc.close();
            out.close();
            System.out.println("Ok");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
