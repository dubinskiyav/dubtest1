package word;//import com.spire.doc.*;

import com.spire.doc.ToPdfParameterList;
import com.spire.pdf.FileFormat;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;


/**
 * Попытка преобразования doc в pdf - не работает
 *  * @see <a href="https://habr.com/ru/sandbox/142728/">habr</a>
 *  * @see <a href="https://www.youtube.com/watch?v=rFDwT1kRpLk&ab_channel=ChargeAhead">youtube</a>
 */
public class WordToPdfSpireDoc {

    public void test1() {
        // Здесь валится
        com.spire.doc.Document document = new com.spire.doc.Document();
        document.loadFromFile("C:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");


        //Сохранить как PDF
        //Create a ToPdfParameterList instance
        ToPdfParameterList ppl=new ToPdfParameterList();
        //Embed all fonts in the PDF document
        ppl.isEmbeddedAllFonts(true);
        //Remove the hyperlinks and keep the character formats
        ppl.setDisableLink(true);

        //Set the output image quality as 40% of the original image. 80% is the default setting.
        document.setJPEGQuality(40);

        document.saveToFile("C:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf", ppl);
        //document.saveToFile("C:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf");
    }
    public void test2() {

        try {
            FileInputStream docFile = new FileInputStream(
                    new File("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx"));
            XWPFDocument doc = new XWPFDocument(docFile);
            PdfOptions pdfOptions = PdfOptions.create();
            OutputStream out = new FileOutputStream(
                    new File("d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf"));
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
    //    https://overcoder.net/q/2640455/%D0%BF%D0%BE%D0%BF%D1%8B%D1%82%D0%BA%D0%B0-%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D1%82%D1%8C-%D0%BF%D1%80%D0%BE%D1%81%D1%82%D0%BE%D0%B9-pdf-%D0%B4%D0%BE%D0%BA%D1%83%D0%BC%D0%B5%D0%BD%D1%82-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-apache-poi
    public void test3() {
        XWPFDocument document = new XWPFDocument();

        // there must be a styles document, even if it is empty
        XWPFStyles styles = document.createStyles();

        // there must be section properties for the page having at least the page size set
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageSz pageSz = sectPr.addNewPgSz();
        pageSz.setW(BigInteger.valueOf(12240)); //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
        pageSz.setH(BigInteger.valueOf(15840)); //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"

        // filling the body
        XWPFParagraph paragraph = document.createParagraph();

        //create table
        XWPFTable table = document.createTable();

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");

        //create CTTblGrid for this table with widths of the 3 columns.
        //necessary for Libreoffice/Openoffice and PdfConverter to accept the column widths.
        //values are in unit twentieths of a point (1/1440 of an inch)
        //first column = 2 inches width
        table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(2*1440));
        //other columns (2 in this case) also each 2 inches width
        for (int col = 1 ; col < 3; col++) {
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(2*1440));
        }

        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");

        //create third row
        XWPFTableRow tableRowThree = table.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        paragraph = document.createParagraph();

        //document must be written so underlaaying objects will be committed
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.write(out);
            document.close();
            document = new XWPFDocument(new ByteArrayInputStream(out.toByteArray()));
            PdfOptions options = PdfOptions.create();
            PdfConverter converter = (PdfConverter)PdfConverter.getInstance();
            // Валится
            converter.convert(document, new FileOutputStream("XWPFToPDFConverterSampleMin.pdf"), options);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("test3 - Ok");
    }

    public void test4() {
        XWPFDocument document = new XWPFDocument();

        // there must be a styles document, even if it is empty
        XWPFStyles styles = document.createStyles();

        // there must be section properties for the page having at least the page size set
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageSz pageSz = sectPr.addNewPgSz();
        pageSz.setW(BigInteger.valueOf(12240)); //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
        pageSz.setH(BigInteger.valueOf(15840)); //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"

        // filling the body
        XWPFParagraph paragraph = document.createParagraph();

        //create table
        XWPFTable table = document.createTable();

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");

        //create CTTblGrid for this table with widths of the 3 columns.
        //necessary for Libreoffice/Openoffice and PdfConverter to accept the column widths.
        //values are in unit twentieths of a point (1/1440 of an inch)
        //first column = 2 inches width
        table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(2*1440));
        //other columns (2 in this case) also each 2 inches width
        for (int col = 1 ; col < 3; col++) {
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(2*1440));
        }

        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");

        //create third row
        XWPFTableRow tableRowThree = table.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        paragraph = document.createParagraph();

        //document must be written so underlaaying objects will be committed
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.write(out);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1) Create options DOCX 2 PDF to select well converter form the registry
        Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);

        // 2) Get the converter from the registry
        IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

        // 3) Convert DOCX 2 PDF
        InputStream docxin= new ByteArrayInputStream(out.toByteArray());
        OutputStream pdfout = null;
        try {
            pdfout = new FileOutputStream(new File("XWPFToPDFXDocReport.pdf"));
            // Не работает
            converter.convert(docxin, pdfout, options);
            docxin.close();
            pdfout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("test4 - Ok");
    }

}
