import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Работа с MS Word и Excel
 */
public class WordExcel {

    public void test() {
        System.out.println("WordExcel.test");
        XWPFDocument doc = readFile("src/main/resources/Файл.docx");
        // Список run-ов документа
        List<XWPFRun> list = getRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            System.out.println(list.get(i).getText(0));
        }
    }

    public XWPFDocument readFile(String inputFileName) {
        // Ввод из шаблона
        System.out.println("readFile...");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(inputFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    "Ошибка при чтении файла: " + e.getMessage());
        }

        //
        // открываем файл и считываем его содержимое в объект XWPFDocument
        XWPFDocument docxFile = null;
        try {
            docxFile = new XWPFDocument(OPCPackage.open(fileInputStream));
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
            for (XWPFRun run : par.getRuns()) {
                // Добавим в список
                list.add(run);
            }
        }
        // Цикл по таблицам документа
        for (XWPFTable tbl : doc.getTables()) {
            // По строкам таблицы
            for (XWPFTableRow row : tbl.getRows()) {
                // По ячейкам таблицы
                for (XWPFTableCell cell : row.getTableCells()) {
                    // По параграфам в ячейке
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        // По run-ам
                        for (XWPFRun run : p.getRuns()) {
                            // Добавим в список
                            list.add(run);
                        }
                    }
                    // цикл по таблицам ячейки
                    for (XWPFTable tbl1 : cell.getTables()) {
                        // По строкам таблицы
                        for (XWPFTableRow row1 : tbl1.getRows()) {
                            // По ячейкам таблицы
                            for (XWPFTableCell cell1 : row1.getTableCells()) {
                                // По параграфам в ячейке
                                for (XWPFParagraph p1 : cell1.getParagraphs()) {
                                    // По run-ам
                                    for (XWPFRun run1 : p1.getRuns()) {
                                        // Добавим в список
                                        list.add(run1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    // Все run-ы параграфа
    public List<XWPFRun> getRunList(XWPFParagraph p) {
        // Добавим все run-ы параграфа и вернем
        return new ArrayList<>(p.getRuns());
    }

    // Все таблицы документа
    public List<XWPFTable> getTableList(XWPFDocument doc) {
        // Добавим все run-ы параграфа и вернем
        return new ArrayList<>(doc.getTables());
    }

}
