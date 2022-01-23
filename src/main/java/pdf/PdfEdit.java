package pdf;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfEdit {

    String baseFolder = "d:/WORK/Programming/dubtest1/src/main/resources";
    String sourceFileName = baseFolder + "/Файл_pdf.pdf";
    String destFileNamePdf = baseFolder + "/Файл_pdf_out.pdf";

    public void test1() {
        try {
            //Create PdfReader instance.
            PdfReader pdfReader =
                    new PdfReader(sourceFileName);
            //Create PdfStamper instance.
            PdfStamper pdfStamper = new PdfStamper(pdfReader,
                    new FileOutputStream(destFileNamePdf));
            //Create BaseFont instance.
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            //Get the number of pages in pdf.
            int pages = pdfReader.getNumberOfPages();
            //Iterate the pdf through pages.
            for(int i=1; i<=pages; i++) {
                //Contain the pdf data.
                PdfContentByte pageContentByte =
                        pdfStamper.getOverContent(i);

                pageContentByte.beginText();
                //Set text font and size.
                pageContentByte.setFontAndSize(baseFont, 32);

                pageContentByte.setTextMatrix(50, 740);

                //Write text
                pageContentByte.showText("w3spoint.com");
                pageContentByte.endText();
            }
            //Close the pdfStamper.
            pdfStamper.close();
            System.out.println("PDF modified successfully.");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
    public void test2(){
// https://kb.itextpdf.com/home/it7kb/ebooks/itext-7-jump-start-tutorial-for-java/chapter-5-manipulating-an-existing-pdf-document

        try {
            PdfReader reader = new PdfReader(sourceFileName);
            PdfDictionary dict = reader.getPageN(1);
            PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
            PdfArray refs = null;
            if (dict.get(PdfName.CONTENTS).isArray()) {
                refs = dict.getAsArray(PdfName.CONTENTS);
            } else if (dict.get(PdfName.CONTENTS).isIndirect()) {
                refs = new PdfArray(dict.get(PdfName.CONTENTS));
            }
            //Get the number of pages in pdf.
            int pages = reader.getNumberOfPages();
            for (int i = 0; i <=pages; i++) {
                //Contain the pdf data.
                PRStream stream = (PRStream) refs.getDirectObject(i);
                byte[] data = PdfReader.getStreamBytes(stream);
                stream.setData(new String(data).replace("run1", "Первый ран").getBytes());
            }
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFileNamePdf));
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }
}
