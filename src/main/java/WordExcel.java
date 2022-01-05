import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.pdf.internal.Converter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Работа с MS Word и Excel
 */
public class WordExcel {

    public void test1() {
        System.out.println("WordExcel.test");
        // Относительно resource
        XWPFDocument doc = readFile("Файл.docx");
        replaceText(doc,"run1", "run111111");
        replaceText(doc,"Run2.", "Run2.2222222222222222");
        replaceText(doc,"Run2.2222222222222222", "Run2.33333333333333");
        //ConvertToPDF(doc);
        ConvertToPDF1(doc);
    }

    public void test() {
        System.out.println("WordExcel.test");
        // Относительно resource
        XWPFDocument doc = readFile("Файл.docx");
        // Абсолютное имя
        // doc = readFile("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");
        System.out.println("!!!!!!!!!!!!!");
        // Список run-ов документа
        List<XWPFRun> list = getRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            System.out.println(list.get(i).getText(0));
        }
        System.out.println("!!!!!!!!!!!!!");
        list = getAllRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            System.out.println(list.get(i).getText(0));
        }
    }

    public void ConvertToPDF(XWPFDocument doc) {
        // Готовим опции
        PdfOptions options = PdfOptions.create();
        try (OutputStream out = new FileOutputStream(new File("Файл.pdf"));) {
            // Конвертируем
            // Не раотает
            PdfConverter.getInstance().convert(doc, out, options);
            System.out.println(out.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //    https://overcoder.net/q/2640455/%D0%BF%D0%BE%D0%BF%D1%8B%D1%82%D0%BA%D0%B0-%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D1%82%D1%8C-%D0%BF%D1%80%D0%BE%D1%81%D1%82%D0%BE%D0%B9-pdf-%D0%B4%D0%BE%D0%BA%D1%83%D0%BC%D0%B5%D0%BD%D1%82-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-apache-poi
    public void ConvertToPDF1(XWPFDocument document) {
        // Готовим опции
        PdfOptions options = PdfOptions.create();
        try (OutputStream out = new FileOutputStream(new File("Файл1.pdf"));) {
            // there must be a styles document, even if it is empty
            XWPFStyles styles = document.createStyles();
            // there must be section properties for the page having at least the page size set
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            CTPageSz pageSz = sectPr.addNewPgSz();
            pageSz.setW(BigInteger.valueOf(12240)); //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
            pageSz.setH(BigInteger.valueOf(15840)); //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"

            // Конвертируем
            PdfConverter converter = (PdfConverter)PdfConverter.getInstance();
            converter.convert(document, new FileOutputStream("XWPFToPDFConverterSampleMin.pdf"), options);
            System.out.println(out.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // http://www.techwaregeeks.com/word-processing/convert-docx-to-pdf-using-java-top-word-to-pdf-java-libraries/
    // https://products.aspose.com/words/java/

    /**
     * Меняет во всем документе текст
     * @param doc - документ
     * @param from - что менять
     * @param to - на что менять
     */
    public void replaceText(XWPFDocument doc, String from, String to) {
        // Список всех run-ов
        List<XWPFRun> runList = getAllRunList(doc);
        // цикл
        for (XWPFRun run: runList) {
            String text = (run.getText(0) != null)?run.getText(0):"" ;
            if (!text.isEmpty() && text.equals(from)) {
                // если не пустой и равный - меняем
                System.out.println("Меняем " + text + " на " + to);
                text = text.replace(text, to);
                run.setText(text,0);
            }
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
                // По ячейкам строки
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

    // Возвращает список run-ов документа рекурсивно
    public List<XWPFRun> getAllRunList(XWPFDocument doc) {
        List<XWPFRun> list = new ArrayList<>();
        // Цикл по параграфам документа
        for (XWPFParagraph par : doc.getParagraphs()) {
            // Цикл по run-ам параграфа. Run - единица текста
            // Добавим в список все run-ы параграфа
            list.addAll(par.getRuns());
        }
        // Цикл по таблицам документа рекурсивно
        for (XWPFTable tbl : getTables(doc) ) {
            list.addAll(getRunList(tbl));
        }
        return list;
    }

    // Возвращает список всех run-ов таблицы без рекурсии
    public List<XWPFRun> getRunList(XWPFTable table) {
        List<XWPFRun> list = new ArrayList<>();
        // По строкам таблицы
        for (XWPFTableRow row : table.getRows()) {
            // По ячейкам строки
            for (XWPFTableCell cell : row.getTableCells()) {
                // По параграфам в ячейке
                for (XWPFParagraph p : cell.getParagraphs()) {
                    // Добавим в список все run-ы параграфа
                    list.addAll(p.getRuns());
                }
            }
        }
        return list;
    }

    // Возвращает все таблицы документа рекурсивно
    public List<XWPFTable> getTables(XWPFDocument doc) {
        List<XWPFTable> list = new ArrayList<>();
        // Цикл по таблицам документа
        for (XWPFTable table : doc.getTables()) {
            // добавим в возвращаемый список
            list.add(table);
            // добавим все рекурсивно
            list.addAll(getTables(table));
        }
        return list;
    }

    // Список всех таблиц таблицы
    // себя не возвращает
    public List<XWPFTable> getTables(XWPFTable tbl) {
        List<XWPFTable> list = new ArrayList<>();
        // По строкам таблицы
        for (XWPFTableRow row : tbl.getRows()) {
            // По ячейкам строки
            for (XWPFTableCell cell : row.getTableCells()) {
                // цикл по таблицам ячейки
                for (XWPFTable tbl1 : cell.getTables()) {
                    // добавим в возвращаемый список
                    list.add(tbl1);
                    // добавим все рекурсивно
                    list.addAll(getTables(tbl1));
                }
            }
        }
        return list;
    }

}
