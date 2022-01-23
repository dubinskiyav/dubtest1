package word;

import com.aspose.words.Document;

public class WordToPdfAspose {
    // https://blog.aspose.com/2020/02/20/convert-word-doc-docx-to-pdf-in-java-programmatically/
    public void test1(){
        // https://purchase.aspose.com/buy
        // Просят 6-30 тысяч долларов за год за одну лицензию
        try {
            Document doc = new Document("d:/WORK/Programming/dubtest1/src/main/resources/Файл.docx");
            doc.save("d:/WORK/Programming/dubtest1/src/main/resources/Файл.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("test1 - Ok");
    }

}
