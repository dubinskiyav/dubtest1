import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Работа с MS Word и Excel
 */
public class WordExcel {

    public void test() {
        System.out.println("WordExcel.test");
        // Относительно resource
        XWPFDocument doc = readFile("Файл.docx");
        // Абсолютное имя
        // doc = readFile("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");
        // Список run-ов документа
        List<XWPFRun> list = getRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            System.out.println(list.get(i).getText(0));
        }
    }

    // Возвращает XWPFDocument, считанный из ресурса с именем inputFileName
    public XWPFDocument readFile(String inputFileName) {
        // Ввод из шаблона
        System.out.println("readFile...");
        InputStream inputStream;
        FileInputStream fileInputStream;
        try {
            // Читаем из ресурса, а не по абсолютному пути
            inputStream = this.getClass().getClassLoader().getResourceAsStream(inputFileName);
            if (inputStream == null) {
                // Пробуем по абсолютному пути
                fileInputStream = new FileInputStream(inputFileName);
                inputStream =fileInputStream;
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Ошибка при чтении файла: " + e.getMessage());
        }
        // открываем файл и считываем его содержимое в объект XWPFDocument
        XWPFDocument docxFile;
        try {
            docxFile = new XWPFDocument(OPCPackage.open(inputStream));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Ошибка при создании XWPFDocument: " + e.getMessage());
        }

        System.out.println("readFile...Ok");
        return docxFile;

    }

    // Возвращает список run-ов документа
    public List<XWPFRun> getRunList(XWPFDocument doc) {
        List<XWPFRun> list = new ArrayList<>();
        // Цикл по параграфам документа
        for (XWPFParagraph par : doc.getParagraphs()) {
            // Цикл по run-ам параграфа. Run - единица текста
            // Добавим в список все run-ы параграфа
            list.addAll(par.getRuns());
        }
        // Цикл по таблицам документа
        for (XWPFTable tbl : doc.getTables()) {
            // По строкам таблицы
            for (XWPFTableRow row : tbl.getRows()) {
                // По ячейкам таблицы
                for (XWPFTableCell cell : row.getTableCells()) {
                    // По параграфам в ячейке
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        // Добавим в список все run-ы параграфа
                        list.addAll(p.getRuns());
                    }
                    // цикл по таблицам ячейки
                    for (XWPFTable tbl1 : cell.getTables()) {
                        // По строкам таблицы
                        for (XWPFTableRow row1 : tbl1.getRows()) {
                            // По ячейкам таблицы
                            for (XWPFTableCell cell1 : row1.getTableCells()) {
                                // По параграфам в ячейке
                                for (XWPFParagraph p1 : cell1.getParagraphs()) {
                                    // Добавим в список все run-ы параграфа
                                    list.addAll(p1.getRuns());
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

}
