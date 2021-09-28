package services;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class UserService {
    UserDao userDao;

    public UserService(){
        userDao = UserDaoImpl.getInstance();
    }

    public User register(User user) throws MessagingException {
        User tempUser = userDao.login(user);
        if (tempUser != null)
            return null;

        userDao.register(user);
        sendWelcomeEmail(user);

        return user;
    }

    public User login(User user){
        User tempUser = userDao.login(user);
        if (tempUser == null)
            return null;

        return tempUser;
    }

    public void sendWelcomeEmail(User user) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mikearcherdev@gmail.com", "Mike1455.");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("mikearcherdev@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("michael.archer@g.austincc.edu"));
        message.setSubject("Welcome to Reimbursement App!");

        String msg = "Your account has been created!\n" +
                     "Username: " + user.getUsername() + "\n" +
                     "Password: " + user.getPassword();

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
