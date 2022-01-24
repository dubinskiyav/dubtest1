package pdf;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;

// https://kb.itextpdf.com/home/it7kb/ebooks/itext-7-jump-start-tutorial-for-java/chapter-1-introducing-basic-building-blocks
// https://api.itextpdf.com/iText7/java/7.0.0/

public class PdfEdit {

    String baseFolder = "d:/WORK/Programming/dubtest1/src/main/resources";
    String sourceFileName = baseFolder + "/Файл_pdf.pdf";
    String imageGP = "./src/main/resources/gp.png";
    String imageGU = "./src/main/resources/gu.png";
    String textNewYork = "./src/main/resources/Нью-Йорк.txt";
    static String CALIBRI_FONT_FILENAME = "./src/main/resources/Calibri.ttf";
    static String CALIBRI_FONT_FILENAME_BOLD = "./src/main/resources/Calibri-Bold.ttf"; // Этот не работает - валится

    public void NewYork() {
        String destFileNamePdf = baseFolder + "/Файл_NewYork.pdf";
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(destFileNamePdf));
            PageSize ps = PageSize.A5;
            Document document = new Document(pdf, ps);
            // Параметры колонок
            float offSet = 36;
            float columnWidth = (ps.getWidth() - offSet * 2 + 10) / 3;
            float columnHeight = ps.getHeight() - offSet * 2;

            // Описание колонок
            Rectangle[] columns = {
                    new Rectangle(offSet - 5, offSet, columnWidth, columnHeight),
                    new Rectangle(offSet + columnWidth, offSet, columnWidth, columnHeight),
                    new Rectangle(
                            offSet + columnWidth * 2 + 5, offSet, columnWidth, columnHeight)};
            document.setRenderer(new ColumnDocumentRenderer(document, columns));
            // Добавляем контекст
            Image gp = new Image(ImageDataFactory.create(imageGP)).setWidth(columnWidth);
            Image gu = new Image(ImageDataFactory.create(imageGU)).setWidth(columnWidth);
            String articleInstagram = new String(
                    Files.readAllBytes(Paths.get(textNewYork)), StandardCharsets.UTF_8);
            // Заполняем
            addArticle(document,
                    "Нью-Йорк - википедия",
                    "Проба от 24.01.2022", gp, articleInstagram);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addArticle(
            Document doc, String title, String author, Image img, String text
    ) {
        try {
            PdfFont font = PdfFontFactory.createFont(CALIBRI_FONT_FILENAME, PdfEncodings.IDENTITY_H);
            Paragraph p1 = new Paragraph(title)
                    .setFont(font)
                    .setFontSize(14);
            doc.add(p1);
            doc.add(img);
            Paragraph p2 = new Paragraph()
                    .setFont(font)
                    .setFontSize(7)
                    // todo - не могу поянть
                    //.setFontColor()
                    .add(author);
            doc.add(p2);
            Paragraph p3 = new Paragraph()
                    .setFont(font)
                    .setFontSize(10)
                    .add(text);
            doc.add(p3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void HellowWorld() {
        String destFileNamePdf = baseFolder + "/Файл_HellowWorld.pdf";
        try {
            File file = new File(destFileNamePdf);
            file.getParentFile().mkdirs();
            // Инифиализация PDF writer
            FileOutputStream fos = new FileOutputStream(destFileNamePdf);
            //PdfWriter writer = new PdfWriter(fos);
            PdfWriter writer = new PdfWriter(destFileNamePdf); // Так тоже работает
            // Инициализация PDF document
            PdfDocument pdf = new PdfDocument(writer);
            // Инициализация document
            Document document = new Document(pdf);
            // Добавление параграфа
            document.add(new Paragraph("Hellow World - Привет, мир!"));
            // Create a PdfFont
            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            // В стандартных фонтах нет русских букв, делаем свой
            font = PdfFontFactory.createFont(CALIBRI_FONT_FILENAME, PdfEncodings.IDENTITY_H);
            CALIBRI_FONT_FILENAME_BOLD = "./src/main/resources/Calibri-Italic.ttf";
            PdfFont bold = PdfFontFactory.createFont(CALIBRI_FONT_FILENAME_BOLD, "Cp1251");
            bold = PdfFontFactory.createFont(CALIBRI_FONT_FILENAME_BOLD, PdfEncodings.IDENTITY_H);
            //PdfFont bold = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
            document.add(new Paragraph("Hellow World - Привет, мир!").setFont(font));
            Paragraph p = new Paragraph("Новый русский параграф: ");
            p.add(new Text("Этот текст по-русски").setFont(font));
            p.add(new Text("-Этот тоже-"));
            // Хрен там - надо установить фонт у всего параграфа
            p.setFont(font);
            document.add(p);
            // Или у всего документа
            document.setFont(font); // Не работает!!!
            // Create a List
            List list = new List()
                    .setSymbolIndent(12)
                    .setListSymbol("\u2022")
                    .setFont(font);
            // Add ListItem objects
            list.add(new ListItem("Я памятник себе воздвиг нерукотворный,"))
                    .add(new ListItem("К нему не зарастет народная тропа,"))
                    .add(new ListItem("Вознесся выше он главою непокорной"))
                    .add(new ListItem("Александрийского столпа."));
            // Add the list
            document.add(new Paragraph("Я памятник себе воздвиг нерукотворный…"));
            document.add(list);

            // Картинки
            Image gp = new Image(ImageDataFactory.create(imageGP));
            Image gu = new Image(ImageDataFactory.create(imageGU));
            p = new Paragraph("Геликон Про")
                    .add(gp)
                    .add(" производит крутые ")
                    .add(gu);
            document.add(p);

            // Таблица
            Table table = new Table(new float[]{5, 12, 3, 10, 6});
            table.setWidth(UnitValue.createPercentValue(100));
            String line = "Пермь; Екатеринбург; Уфа; Челябинск; Тюмень";
            process(table, line, bold, true);
            java.util.List<String> list1 = Arrays.asList(
                    "1; 2; 3; 4; 5",
                    "1; 2; 3; 4; 5",
                    "1; 2; 3; 4; 5",
                    "1; 2; 3; 4; 5",
                    "1; 2; 3; 4; 5"
            );
            for (String element : list1) {
                process(table, element, font, true);
            }
            // Добавим таблицу
            document.add(table);
            // Закрываем документ
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Добавляет строку в таблицу из строки
    public void process(Table table, String line, PdfFont font, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else {
                table.addCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }
    }
}
