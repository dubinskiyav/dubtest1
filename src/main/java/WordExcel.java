import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Работа с MS Word и Excel
 */
public class WordExcel {

    public void test(){
        System.out.println("WordExcel.test");
    }

    public XWPFDocument readFile(String inputFileName) {
        // Ввод из шаблона
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(inputFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //
        // открываем файл и считываем его содержимое в объект XWPFDocument
        XWPFDocument docxFile = null;
        try {
            docxFile = new XWPFDocument(OPCPackage.open(fileInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return docxFile;

    }

}
