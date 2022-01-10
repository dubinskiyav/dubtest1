import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


// Работает!!!
public class WordToPdf3 {
    // https://question-it.com/questions/695424/preobrazovanie-word-docx-v-pdf-s-pomoschju-apache-poi-i-itext
    // Перед запуском вам необходимо установить офис MS
    public void test() {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        String baseFolder = "d:/WORK/Programming/dubtest1/src/main/resources";
        String fileName = baseFolder + "/Файл.docx";
        String fileNamePdf = baseFolder + "/Файл.pdf";

        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(baseFolder))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
        if (true) {
            Future<Boolean> conversion = converter
                    .convert(in).as(DocumentType.MS_WORD)
                    .to(bo).as(DocumentType.PDF)
                    .prioritizeWith(1000) // optional
                    .schedule();
            try {
                conversion.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(conversion.isDone());
        } else {
            Boolean b = converter
                    .convert(in).as(DocumentType.MS_WORD)
                    .to(bo).as(DocumentType.PDF)
                    .prioritizeWith(1000) // optional
                    .execute();
            System.out.println(b);
        }

        try (OutputStream outputStream = new FileOutputStream(fileNamePdf)) {
            bo.writeTo(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            in.close();
            bo.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("test - Ok");
    }

}
