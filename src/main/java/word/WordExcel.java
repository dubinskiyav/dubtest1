package word;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.pdf.internal.Converter;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Работа с MS Word и Excel
 */
public class WordExcel {

    public void testTable() {
        System.out.println("word.WordExcel.testTable");
        // Относительно resource
        XWPFDocument doc = readFile(
                "d:/WORK/DATA/Projects/РКС/Техническое_присоединение/Documents/2021/Карточки задач/72271 Печать заявления на выдачу ТУ/"
                        + "shablon_zapros_o_vidache_tu_queue.docx");
        doc = readFile(
                "d:/WORK/DATA/Projects/РКС/Техническое_присоединение/Documents/2021/Карточки задач/72271 Печать заявления на выдачу ТУ/"
                        + "shablon_zapros_o_vidache_tu.docx");
        // По всем параграфам
        // Список всех параграфов
        List<XWPFParagraph> paragraphList = getAllParagraphs(doc);
        for (XWPFParagraph p : paragraphList) {
            boolean dollar = false; // Знак доллара
            int dollarPos = -1;
            boolean curlyBraceOpens = false; // Фигурная скобка откр
            for (XWPFRun r : p.getRuns()) {
                // Ищем доллар
                if (!dollar) { // мы еще не в поиске
                    String s = getText(r);
                    if (s.contains("$")) {
                        dollar = true; // доллар найден
                        dollarPos = s.indexOf("$");
                        if (dollarPos < s.length()) {
                            if (s.substring(dollarPos+1,1).equals("{")) {
                                curlyBraceOpens = true; // Нашли ${
                                int i = s.indexOf("}", dollarPos+2);
                                if (i != -1) {
                                    // В этом ране есть метка в формате ${name}
                                    // ничего не делаем
                                    dollar = false; // будем искать дальше
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("!!!!!!!!!!!!!");
        // Количество очередей
        int queuecount = 3;
        if (false) { // Не таблицы - заменяем параграф
            XWPFParagraph par = findParagraph(doc, "xml_show_name__1");
            if (par != null) {
                System.out.println("нашли " + par.getText());
                XmlCursor cursor = par.getCTP().newCursor();
                int curQueue = 1;
                do {
                    System.out.println(curQueue);
                    XWPFParagraph newP = doc.createParagraph();
                    newP.getCTP().setPPr(par.getCTP().getPPr());
                    XWPFRun newR = newP.createRun();
                    newR.getCTR().setRPr(par.getRuns().get(0).getCTR().getRPr());
                    newR.setText("очередь № " + curQueue + ": ${xml_show_name__" + curQueue + "}");
                    XmlCursor c2 = newP.getCTP().newCursor();
                    c2.moveXml(cursor);
                    c2.dispose();
                    curQueue++;
                } while (curQueue <= queuecount);
                cursor.removeXml(); // Removes replacement text paragraph
                cursor.dispose();
            }
        } else {
            String s = "xml_addconnectloadparamdata_value_05__";


            makeSimpleTable(doc, queuecount, "xml_show_name__");
            makeSimpleTable(doc, queuecount, "xml_statementtc_connectobjname__");
            makeDifficultTable(doc, queuecount, "xml_infmaxparam1__");
            makeSimpleTable(doc, queuecount, "xml_statementtc_dateplan__");
            makeDifficultTable(doc, queuecount, "xml_addconnectloadparamdata_value_05_3__");
            replaceLabelDollar(doc, "requestor_name", "Рога");
            replaceLabelDollar(doc, "xml_statementtc_connectobjname__2", "Мадагаскар");
            replaceLabelDollar(doc, "xml_infmaxparam1__1", "Километр");

            replaceLabelDollar(doc, "xml_infmaxparam1", "Километовая высота");
            replaceLabelDollar(doc, "xml_infmaxparam2", "Большая этажность");
            replaceLabelDollar(doc, "xml_infmaxparam3", "Офигенная протяженность");
            replaceLabelDollar(doc, "xml_infmaxparam4", "Огромный диаметр");
            replaceLabelDollar(doc, "xml_infmaxparam1__1", "ЫЫЫЫЫЫЫЫЫЫЫЫЫЫЫ");
            replaceLabelDollar(doc, "xml_statementtc_dateplan__2", "Проба дааааааа");

        }
        FileOutputStream out;
        try {
            out = new FileOutputStream("D:/TEMP/shablon_zapros_o_vidache_tu_queue.docx");
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создание слоожной таблицы
     * @param doc - документ
     * @param queuecount - количество очередей
     * @param label - метка для определения таблицы, без 1 на конце
     */
    private void makeDifficultTable(XWPFDocument doc, int queuecount, String label) {
        String labelDollar = "${" + label + "1}";
        // Найдем нужную таблицу
        XWPFTable t = findTableByText(doc, labelDollar);
        if (t == null) {
            return;
        }
        // Список по колонкам со строками-параграфами
        List<String[]> ls = new ArrayList<>();
        int currRowNumb = 1; // Считаем, что одна строчка под одну очередь уже есть
        do {
            XWPFTableRow row = t.getRows().get(t.getNumberOfRows() - 1);
            // Кол-во колонок
            int colCount = row.getTableCells().size();
            // Бежим по всем колонкам
            for (int i = 0; i < colCount; i++) {
                // Получим ячейку
                XWPFTableCell c = row.getTableCells().get(i);
                XWPFRun run = null;
                if (currRowNumb == 1) { // Это первая строка - запоминаем абзацы
                    String[] ss = c.getParagraphs().stream().map(XWPFParagraph::getText)
                            .toArray(String[]::new);
                    ls.add(ss);
                    // Больше ничего не делаем - оставляем эту строчку
                } else {
                    // Это вторая строка
                    if (i == 0) { // Это первая ячейка типа "Очередь № 1"
                        XWPFParagraph p = c.getParagraphs().get(0);
                        // Добавим run в параграф
                        run = p.createRun();
                        run.setText("очередь № " + currRowNumb, 0);
                    } else {
                        // Это не первая ячейка
                        // Надо добавить столько параграфов сколько запомнено
                        // Предварительно убалив параграф
                        while (c.getParagraphs().size() > 0) {
                            c.removeParagraph(0);
                        }
                        for (String text : ls.get(i)) {
                            XWPFParagraph p = c.addParagraph();
                            // Ищем есть ли что заменять
                            if (text.indexOf("${") < text.indexOf("}")) {
                                // Есть что заменять
                                // Заменим последнюю цифру на номер строчки
                                String s1 = text.substring(0,text.indexOf("${"));
                                String s2 = text.substring(text.indexOf("}"));
                                String s3 = text.substring(text.indexOf("${"), text.indexOf("}") - 1);
                                text = s1 + s3 + currRowNumb + s2;
                            }
                            setText(p, text);
                        }
                    }
                }
                if (currRowNumb > 1) {
                    if (false) { // todo - сделать
                        // ран у ячейки выше
                        XWPFRun prevRun = t.getRow(currRowNumb - 2).getCell(i).getParagraphs()
                                .get(0)
                                .getRuns().get(0);
                        // установим форматы у текущей как у предыдущей
                        copyFormat(prevRun, run);
                    }
                }
            }
            currRowNumb++;
            if (currRowNumb < queuecount + 1) {
                // Добавим строчку
                t.createRow();
            }
        } while (currRowNumb < queuecount + 1);
    }

    /**
     * Создает простую таблицу
     *
     * @param doc        - документ
     * @param queuecount - количество очередей
     * @param label      - метка таблицы без долларов и без единицы в конце
     */
    private void makeSimpleTable(XWPFDocument doc, int queuecount, String label) {
        String labelDollar = "${" + label + "1}";
        // Список всех таблиц документа без рекурсии
        List<XWPFTable> xwpfTableList = doc.getTables();
        // Найдем нужную таблицу
        XWPFTable t = findTableByText(doc, labelDollar);
        if (t == null) {
            return;
        }
        int currRowNumb = 1; // Считаем, что одна строчка под одну очередь уже есть
        do {
            XWPFTableRow row = t.getRows().get(t.getNumberOfRows() - 1);
            // Кол-во колонок
            int colCount = row.getTableCells().size();
            // Бежим по всем колонкам
            for (int i = 0; i < colCount; i++) {
                // Получим ячейку
                XWPFTableCell c = row.getTableCells().get(i);
                // Получим параграф ячейки - это добавляет параграф, если его не было
                XWPFParagraph paragraph = c.getParagraphs().get(0);
                XWPFRun run;
                if (currRowNumb == 1) {
                    // В параграфе может быть несколько ранов
                    if (paragraph.getRuns().size() > 1) {
                        // если не один - все удаляем, кроме первого
                        removeAllRuns(paragraph, 1);
                        // При этом скорее всего теряем форматирование
                    }
                    // берем первый ран
                    run = paragraph.getRuns().get(0);
                } else {
                    // Добавим run в параграф
                    run = paragraph.createRun();
                }
                // Установим текст
                switch (i) {
                    case (0):
                        run.setText("очередь № " + currRowNumb, 0);
                        break;
                    default:
                        run.setText("${" + label + currRowNumb + "}", 0);
                        break;
                }
                if (currRowNumb > 1) {
                    // ран у ячейки выше
                    XWPFRun prevRun = t.getRow(currRowNumb - 2).getCell(i).getParagraphs()
                            .get(0)
                            .getRuns().get(0);
                    // установим форматы у текущей как у предыдущей
                    copyFormat(prevRun, run);
                }
            }
            currRowNumb++;
            if (currRowNumb < queuecount + 1) {
                // Добавим строчку
                t.createRow();
            }
        } while (currRowNumb < queuecount + 1);
    }

    /**
     * Список всех параграфов документа включая таблицы и вложенные на один уровень таблицы в
     * таблицы
     *
     * @param doc - документ
     * @return - список, всегда не null;
     */
    private List<XWPFParagraph> getAllParagraphs(XWPFDocument doc) {
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

    /**
     * Ищет параграф, содержащий метку
     *
     * @param doc   - документ
     * @param label - метка без доллара и скобок
     * @return - параграф, содержащий ${label} или null
     */
    private XWPFParagraph findParagraph(XWPFDocument doc, String label) {
        label = "${" + label + "}";
        // Список всех параграфов
        List<XWPFParagraph> paragraphList = getAllParagraphs(doc);
        for (XWPFParagraph p : paragraphList) {
            String s = getText(p);
            if (s.contains(label)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Ищет ран, содержащий метку
     *
     * @param doc   - документ
     * @param label - метка без доллара и скобок
     * @return - ран, содержащий ${label} или null
     */
    private XWPFRun findRun(XWPFDocument doc, String label) {
        label = "${" + label + "}";
        // Список всех параграфов
        List<XWPFParagraph> paragraphList = getAllParagraphs(doc);
        for (XWPFParagraph p : paragraphList) {
            for (XWPFRun r : p.getRuns()) { // Все рану параграфа
                String s = getText(r);
                if (s.contains(label)) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Устанавливает текст у параграфа. Удаляет все предварительно
     *
     * @param p    - параграф
     * @param text - текст
     */
    private void setText(XWPFParagraph p, String text) {
        if (p.getRuns().size() > 0) {
            removeAllRuns(p);
        }
        XWPFRun run = p.createRun();
        run.setText(text, 0);
    }

    /**
     * Удяляет все рану у параграфа
     *
     * @param p - параграф
     */
    private void removeAllRuns(XWPFParagraph p) {
        removeAllRuns(p, 0);
    }

    /**
     * Удяляет все рану у параграфа, кроме первых нескольких
     *
     * @param p          - параграф
     * @param countFirst - сколько первых ранов оставить
     */
    private void removeAllRuns(XWPFParagraph p, int countFirst) {
        // Удалим все рану параграфа
        int numberOfRuns = p.getRuns().size() - countFirst;
        for (int j = 0; j < numberOfRuns; j++) {
            p.removeRun(numberOfRuns - j - 1);
        }
    }

    private String getText(XWPFParagraph p) {
        // цикд по ранам
        List<XWPFRun> runList = p.getRuns();
        StringBuilder ret = new StringBuilder();
        for (XWPFRun r : runList) {
            try {
                String s = r.getText(0);
                ret.append(s);
            } catch (Exception e) {
                System.out.println("Не смогли");
            }
        }
        return ret.toString();
    }

    private String getText(XWPFRun r) {
        try {
            String s = r.getText(0);
            if (s == null) {
                s = "";
            }
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * заменяет метку на текст в документе
     *
     * @param doc   - документ
     * @param label - метка, которую заменять, в шаблоне метка должна быть в скобках и долларе
     *              пример ${show_name}, а здесь нет, просто show_name
     * @param text  - на что заменять
     * @return заменили или нет
     */
    private boolean replaceLabelDollar(XWPFDocument doc, String label, String text) {
        if (doc == null || label == null || text == null) {
            return false;
        }
        boolean replaceFlag = false;
        // Пытаемся сначала заменить в ране
        XWPFRun r = findRun(doc, label);
        if (r != null) {
            label = "${" + label + "}";
            String partext = getText(r);
            // Заменим
            partext = partext.replace(label, text);
            r.setText(partext, 0);
            return true;
        }

        XWPFParagraph p = findParagraph(doc, label);
        if (p != null) {
            label = "${" + label + "}";
            String partext = getText(p);
            // Запомним формат из первого рана
            int fontSize = p.getRuns().get(0).getFontSize();
            String fontFamily = p.getRuns().get(0).getFontFamily();
            boolean isBold = p.getRuns().get(0).isBold();
            // Заменим
            partext = partext.replace(label, text);
            // Удалим все рану параграфа
            removeAllRuns(p);
            int numberOfRuns = p.getRuns().size();
            for (int j = 0; j < numberOfRuns; j++) {
                p.removeRun(numberOfRuns - j - 1);
            }
            // Добавим один ран
            XWPFRun run = p.createRun();
            run.setText(partext);
            // Формат как у первого рана, который мы запомнили
            if (fontSize > 0) {
                run.setFontSize(fontSize);
            }
            run.setFontFamily(fontFamily);
            run.setBold(isBold);
            replaceFlag = true;
        }
        return replaceFlag;
    }


    /**
     * Устанавливает формат ячейки как у ячейки
     *
     * @param from - фром
     * @param to   - ту
     */
    private void copyFormat(XWPFRun from, XWPFRun to) {
        if (from == null || to == null) {
            return;
        }
        // установим форматы у текущей как у предыдущей
        int fontSize = from.getFontSize();
        if (fontSize > 0) {
            to.setFontSize(fontSize);
        }
        //to.setFontSize(from.getFontSizeAsDouble());
        to.setFontFamily(from.getFontFamily());
        to.setBold(from.isBold());
        to.setColor(from.getColor());
    }

    /**
     * Возвращает первую попавшуюся таблицу из документа, содержащую текст без вложенных таблиц
     *
     * @param d     - документ
     * @param label - текст
     * @return - таблица или null
     */
    private XWPFTable findTableByText(XWPFDocument d, String label) {
        if (d == null || label == null) {
            return null;
        }
        List<XWPFTable> tl = d.getTables();
        XWPFTable tbl = tl.stream()
                .filter(tt -> tt.getText().contains(label))
                .findFirst().orElse(null);
        if (tbl != null) {
            // нашли таблицу
            return tbl;
        }
        // Пробуем найти во вложенных таблицах
        for (XWPFTable t : tl) { // По таблицам
            for (XWPFTableRow r : t.getRows()) { // По строкам
                for (XWPFTableCell c : r.getTableCells()) { // По ячейкам
                    for (XWPFTable t1 : c.getTables()) {// По таблицам
                        for (XWPFTableRow r1 : t1.getRows()) { // По строкам
                            for (XWPFTableCell c1 : r1.getTableCells()) { // По ячейкам
                                for (XWPFParagraph p1 : c1.getParagraphs()) { // По параграфам
                                    String text = getText(p1);
                                    if (text.contains(label)) {
                                        return t; // Вернем основную таблицу
                                        //return t1;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        // Ничего не нашли
        return null;
    }

    public void test1() {
        System.out.println("word.WordExcel.test");
        // Относительно resource
        XWPFDocument doc = readFile("Файл.docx");
        replaceText(doc, "run1", "run111111");
        replaceText(doc, "Run2.", "Run2.2222222222222222");
        replaceText(doc, "Run2.2222222222222222", "Run2.33333333333333");
        //ConvertToPDF(doc);
        ConvertToPDF1(doc);
    }

    public void test() {
        System.out.println("word.WordExcel.test");
        // Относительно resource
        XWPFDocument doc = readFile("Файл.docx");
        doc = readFile("shablon_zapros_o_vidache_tu.docx");
        // Абсолютное имя
        // doc = readFile("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");
        System.out.println("!!!!!!!!!!!!!");
        // Список run-ов документа
        List<XWPFRun> list = getRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            System.out.println("run " + i + ": " + list.get(i).getText(0));
        }
        System.out.println("!!!!!!!!!!!!!");
        list = getAllRunList(doc);
        for (int i = 0; i <= list.size() - 1; i++) {
            //System.out.println(list.get(i).getText(0));
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
            pageSz.setW(
                    BigInteger.valueOf(12240)); //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
            pageSz.setH(BigInteger.valueOf(15840)); //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"

            // Конвертируем
            PdfConverter converter = (PdfConverter) PdfConverter.getInstance();
            converter.convert(document, new FileOutputStream("XWPFToPDFConverterSampleMin.pdf"),
                    options);
            System.out.println(out.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // http://www.techwaregeeks.com/word-processing/convert-docx-to-pdf-using-java-top-word-to-pdf-java-libraries/
    // https://products.aspose.com/words/java/
    // Стоит $5999 в год

    /**
     * Меняет во всем документе текст
     *
     * @param doc  - документ
     * @param from - что менять
     * @param to   - на что менять
     */
    public void replaceText(XWPFDocument doc, String from, String to) {
        // Список всех run-ов
        List<XWPFRun> runList = getAllRunList(doc);
        // цикл
        for (XWPFRun run : runList) {
            String text = (run.getText(0) != null) ? run.getText(0) : "";
            if (!text.isEmpty() && text.equals(from)) {
                // если не пустой и равный - меняем
                System.out.println("Меняем " + text + " на " + to);
                text = text.replace(text, to);
                run.setText(text, 0);
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
        for (XWPFTable tbl : getTables(doc)) {
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
