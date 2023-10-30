package mail;

import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class MailTest {

    public void send() throws MessagingException, IOException {
        System.out.println("Send mail test...");
        Properties p = new Properties();
        String charset = "windows-1251";
        p.put("mail.user", "dav");
        p.put("mail.password", "12345");
        p.put("mail.from", "dav@gelicon.biz");
        String address = "dubinskiyav@gmail.com";
        String text = "Вы выиграли счастливый билет!";
        String subject = "Привет!";
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        p.getProperty("mail.user"),
                        p.getProperty("mail.password")
                );
            }
        };
        Session session = Session.getInstance(p, authenticator);
        MimeMessage message = new MimeMessage(session);
        Address addressFrom = new InternetAddress(p.getProperty("mail.from"));
        message.setFrom(addressFrom);
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(subject, charset);

        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(text, "text/plain; charset=" + charset);
        multipart.addBodyPart(body);

        byte[] data = "Это вложение".getBytes();
        MimeBodyPart appendix = new MimeBodyPart();
        DataSource dataSource = new BlobDataSource(
                data,
                "вложение 1"
        );
        appendix.setDataHandler(new DataHandler((jakarta.activation.DataSource) dataSource));
        appendix.setFileName(MimeUtility.encodeText(dataSource.getName()));
        multipart.addBodyPart(appendix);

        Transport.send(message);

        System.out.println("Send mail test...Ok");
    }

    private static class BlobDataSource implements DataSource {

        private final byte[] data;
        private final String name;

        BlobDataSource(byte[] data, String name) throws IOException {
            if (data == null)
                throw new IOException(String.format("Вложение \"%s\" пустое", name));
            this.data = data;
            this.name = name;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return name;
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException();
        }

        @Override
        public String getContentType() {
            return "bin";
        }

    }

}
