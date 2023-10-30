package word;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordToPdfGelicon {

    String baseFolder = "C:/WORK/Programming/dubtest1/src/main/resources/";
    static String CALIBRI_FONT_FILENAME = "./src/main/resources/Calibri.ttf";
    static String CALIBRI_FONT_FILENAME_BOLD = "./src/main/resources/Calibri-Bold.ttf"; // Этот не работает - валится

    public void test() {
        String fileName = "request";
        XWPFDocument doc = readFile(baseFolder + fileName + ".docx");
        String destFileNamePdf = baseFolder + fileName + ".pdf";
        File file = new File(destFileNamePdf);
        // Инифиализация PDF writer
        try {
            FileOutputStream fos = new FileOutputStream(destFileNamePdf);
            PdfWriter writer = new PdfWriter(destFileNamePdf); // Так тоже работает
            // Инициализация PDF document
            PdfDocument pdf = new PdfDocument(writer);
            // Инициализация document
            com.itextpdf.layout.Document document = new Document(pdf);
            // В стандартных фонтах нет русских букв, делаем свой
            PdfFont font = PdfFontFactory.createFont(CALIBRI_FONT_FILENAME, PdfEncodings.IDENTITY_H);
            CALIBRI_FONT_FILENAME_BOLD = "./src/main/resources/Calibri-Italic.ttf";
            // По всем элементам
            for (IBodyElement e : doc.getBodyElements()) {
                //System.out.println(e);
                //System.out.println(e.getClass());
                if (e instanceof org.apache.poi.xwpf.usermodel.XWPFParagraph) {
                    XWPFParagraph xwpfParagraph = (XWPFParagraph) e;
                    // Добавление параграфа
                    Paragraph p = new Paragraph(xwpfParagraph.getText());
                    // надо установить фонт у всего параграфа
                    p.setFont(font);
                    document.add(p);
                } else if (e instanceof org.apache.poi.xwpf.usermodel.XWPFTable) {
                    // Это таблицы
                    XWPFTable t = (XWPFTable) e;
                    // Добавление таблицы
                    // Число колонок такое же как у первой строки
                    Table table = new Table(t.getRows().get(0).getTableCells().size());
                    // Все строки таблицы
                    for (XWPFTableRow r : t.getRows()) {
                        // Все ячейки строки
                        for (XWPFTableCell c : r.getTableCells()) {
                            Cell cell = new Cell();
                            // Все параграфы ячейкм
                            for (XWPFParagraph p : c.getParagraphs()) {
                                Paragraph par = new Paragraph(p.getText());
                                cell.add(par);
                                if (p.getText().equals("Объект номер 111")) {
                                    System.out.println("Объект номер 111");
                                }
                            }
                            table.addCell(cell);
                        }
                    }
                    table.setFont(font);
                    document.add(table);
                }
            }
            // Закрываем документ
            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Возвращает XWPFDocument, считанный из ресурса с именем inputFileName
    public XWPFDocument readFile(String inputFileName) {
        // Ввод из шаблона
        InputStream inputStream;
        FileInputStream fileInputStream;
        try {
            // Читаем из ресурса, а не по абсолютному пути
            inputStream = this.getClass().getClassLoader().getResourceAsStream(inputFileName);
            if (inputStream == null) {
                // Пробуем по абсолютному пути
                fileInputStream = new FileInputStream(inputFileName);
                inputStream = fileInputStream;
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

        return docxFile;
    }

    /**
     * Список всех параграфов документа включая таблицы и вложенные на один уровень таблицы в
     * таблицы
     *
     * @param doc - документ
     * @return - список, всегда не null;
     */
    private List<XWPFParagraph> getAllParagraphs(XWPFDocument doc) {
        for (IBodyElement e : doc.getBodyElements()) {
            System.out.println(e);
            System.out.println(e.getClass());

        }
        // Список всех параграфов
        List<XWPFParagraph> paragraphList = new ArrayList<>();
        if (doc == null) {
            return paragraphList;
        }
        paragraphList.addAll(doc.getParagraphs());
        // Все таблиц документа без рекурсии
        for (XWPFTable t : doc.getTables()) {
            // Все строки таблицы
            for (XWPFTableRow r : t.getRows()) {
                // Все ячейки строки
                for (XWPFTableCell c : r.getTableCells()) {
                    // Все параграфы ячейкм
                    for (XWPFParagraph p : c.getParagraphs()) {
                        paragraphList.add(p);
                    }
                    // Все таблицы ячейки - одна вложенность
                    for (XWPFTable t1 : c.getTables()) {
                        // Все строки таблицы
                        for (XWPFTableRow r1 : t1.getRows()) {
                            // Все ячейки строки
                            for (XWPFTableCell c1 : r1.getTableCells()) {
                                // Все параграфы ячейкм
                                for (XWPFParagraph p : c1.getParagraphs()) {
                                    paragraphList.add(p);
                                }
                            }
                        }
                    }
                }
            }
        }
        return paragraphList;
    }

}
