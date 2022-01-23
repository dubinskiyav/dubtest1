import pdf.PdfEdit;
import word.WordExcel;
import word.WordToPdfApachePOI;
import word.WordToPdfAspose;
import word.WordToPdfDocuments4j;
import word.WordToPdfDocx4j;
import word.WordToPdfSpireDoc;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        if (true) {
            PdfEdit pdfEdit = new PdfEdit();
            //pdfEdit.test1();
            pdfEdit.test2();
        }
        if (false) {
            WordToPdfDocx4j wordToPdfDocx4j = new WordToPdfDocx4j();
            wordToPdfDocx4j.test();
        }
        if (false) {
            WordToPdfDocuments4j wordToPdfDocuments4j = new WordToPdfDocuments4j();
            wordToPdfDocuments4j.test();
        }
        if (false) {
            WordToPdfApachePOI wordToPdfApachePOI = new WordToPdfApachePOI();
            wordToPdfApachePOI.test1();
            //wordToPdf2.test2();
        }
        if (false) {
            WordToPdfAspose wordToPdfAspose = new WordToPdfAspose();
            wordToPdfAspose.test1();
        }
        if (false) {
            WordToPdfSpireDoc wordToPdfSpireDoc = new WordToPdfSpireDoc();
            wordToPdfSpireDoc.test1();
            //wordToPdfSpireDoc.test2();
            //wordToPdfSpireDoc.test3();
            //wordToPdfSpireDoc.test4();
        }
        if (false) {
            WordExcel wordExcel = new WordExcel();
            //wordExcel.test();
            wordExcel.test1();
        }
        if (false) {
            XMLParseTest xmlParseTest = new XMLParseTest();
            xmlParseTest.test();
        }
        if (false) {
            BitTest bitTest = new BitTest();
            //bitTest.test1();
            bitTest.test2();
            int VISIBLE_FLAG = 0b0100;
            System.out.println("VISIBLE_FLAG = " + VISIBLE_FLAG);
        }
        if (false) {
            ArrayListExample arrayListExample = new ArrayListExample();
            arrayListExample.test1();

            GenericsExample genericsExample = new GenericsExample();
            genericsExample.test();
            ComparableExample comparableExample = new ComparableExample();
            comparableExample.test1();
            comparableExample.test2();
            comparableExample.test3();
            comparableExample.test4();
        }
        if (false) {
            StreamAPIExample streamAPIExample = new StreamAPIExample();
            streamAPIExample.test1();
            streamAPIExample.test2();
            streamAPIExample.test3();
            streamAPIExample.test4();
            streamAPIExample.test5();
            streamAPIExample.test6();
            streamAPIExample.test7();

            ArrayExample arrayExample = new ArrayExample();
            arrayExample.test1();
        }
        if (false) {
            ExceptionExample exceptionExample = new ExceptionExample();
            //exceptionExample.test1();
            //exceptionExample.test2();
            try {
                exceptionExample.test3();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (false) {
            StreamAPIExample streamAPIExample = new StreamAPIExample();
            streamAPIExample.test9();

        }
        {
            ArrayListExample arrayListExample = new ArrayListExample();
            //arrayListExample.test2();
            //arrayListExample.test3();
            //arrayListExample.test4();
        }
        {
            BoxingTest boxingTest = new BoxingTest();
            //boxingTest.test1();
            //boxingTest.test2();
        }
        if (false) {
            DatabaseTest databaseTest = new DatabaseTest();
            databaseTest.Connect();
            databaseTest.Select1();
        }
        if (false) {
            InterfaceTest interfaceTest = new InterfaceTest();
            interfaceTest.test1();
        }
        if (false) {
            StreamCreateTest streamCreateTest = new StreamCreateTest();
            streamCreateTest.test1();
        }
        if (false) {
            LambdaTest lambdaTest = new LambdaTest();
            lambdaTest.test1();
        }
    }

}
