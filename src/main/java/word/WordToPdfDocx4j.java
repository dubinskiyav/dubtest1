package word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

public class WordToPdfDocx4j {

    // https://www.codegrepper.com/code-examples/whatever/doc+to+pdf
    // Не работает
    public void test(){
        try {
            InputStream templateInputStream =
                    new FileInputStream("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");

            // Здесь валится
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            String outputfilepath = "d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf";
            FileOutputStream os = new FileOutputStream(outputfilepath);
            Docx4J.toPDF(wordMLPackage,os);
            os.flush();
            os.close();
        } catch (Throwable e) {

            e.printStackTrace();
        }
    }

}
