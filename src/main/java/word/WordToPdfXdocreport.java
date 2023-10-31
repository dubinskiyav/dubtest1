package word;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Рабочая конвертация
 */

public class WordToPdfXdocreport {

    public void test() {
        String baseFolder = "C:/WORK/Programming/dubtest1/src/main/resources";
        String fileName = baseFolder + "/Файл1.docx";
        String fileNamePdf = baseFolder + "/Файл1.pdf";
        try(InputStream inputStream = new FileInputStream(fileName);
                OutputStream outputStream = new FileOutputStream(fileNamePdf)) {
            XWPFDocument document = new XWPFDocument(inputStream);
            PdfOptions options = PdfOptions.create();
            // Convert .docx file to .pdf file
            PdfConverter.getInstance().convert(document, outputStream, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("test - Ok");
    }

}
