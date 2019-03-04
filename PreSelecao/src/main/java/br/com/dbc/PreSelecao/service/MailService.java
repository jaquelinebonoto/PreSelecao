package br.com.dbc.PreSelecao.service;

/**
 *
 * @author jaqueline.bonoto
 */
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    //foi utilizado MimeMessage, atributo da classe JavaMailSender, que por sua vez implementa a interface MailSender
    public void sendMail(String from, String to, String subject, String htmlMsg) {

        MimeMessagePreparator preparator = (MimeMessage mimeMessage) -> {
            mimeMessage.setFrom(
                    new InternetAddress("emailparatestarfuncionalidades@gmail.com"));
            mimeMessage.setRecipient(
                    Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(
                    subject);
            mimeMessage.setContent(htmlMsg, "text/html");
        };
        javaMailSender.send(preparator);
    }
}
