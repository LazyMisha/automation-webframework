package com.project.utils;

import com.project.logger.Log;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class EMail {

    private static String email = "";
    private static String password = "";

    /**
     * send email
     * @param subject - subject of email
     * @param text - body (text) of email
     * @param addReport - if 'true' the report (allure-results) will be zipped and send to email
     * */
    public static void send(String subject, String text, boolean addReport) {
        Properties properties = FileSystem.getProperties("Run.config.properties");
        initializePasswordAndEmail(properties);
        Session session = proceedAuthentication(properties);
        sendEmail(session, subject, text, addReport, properties.getProperty("recipients").split(","));
    }

    /**
     * just initialize the variables 'email' and 'password'
     * @param properties - data from 'Run.config.properties' file - email, password
     * */
    private static void initializePasswordAndEmail(Properties properties) {
        try {
            email = properties.getProperty("email");
            password = properties.getProperty("password");
        } catch (NullPointerException e) {
            Log.error("Can not load 'email' or 'password' from = /resources/Run.config.properties", e);
        }
    }

    /**
     * proceed authentication
     * invoke after method 'initializePasswordAndEmail(P p)' and before method 'sendEmail(S s, S s, S s, B b, P p)'
     * @param properties - data from 'Run.config.properties' file -
     *      mail.smtp.host
     *      mail.smtp.port
     *      mail.smtp.auth
     *      mail.smtp.starttls.enable
     *      mail.smtp.ssl.trust
     * */
    private static Session proceedAuthentication(Properties properties) {
        return Session.getInstance(Objects.requireNonNull(properties), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    /**
     * send email
     * @param session - return method 'proceedAuthentication(P p)'
     * @param subject - subject of email
     * @param text - body (text) of email
     * @param addReport - if 'true' the report (allure-results) will be zipped and send to email
     * @param to - list of recipients from 'Run.config.properties' file separated by comma (,)
     * */
    private static void sendEmail(Session session, String subject, String text, boolean addReport, String... to) {
        try {
            InternetAddress[] recipientAddress = new InternetAddress[to.length];
            int counter = 0;
            for (String recipient : to) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html");

            if (addReport) {
                setReportToEmail(mimeBodyPart);
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
            Log.info("Email sent from '" + email + "\n" + "to " + Arrays.toString(to));
            FileSystem.deleteFile(FileSystem.getPathToZipReport());
        } catch (MessagingException e){
            Log.error("Email not sent", e);
        }
    }

    /**
     * zip 'allure-report'
     * set to the email as attachment
     * @param mimeBodyPart - set zip report to (MimeBodyPart) object
     * */
    private static void setReportToEmail(MimeBodyPart mimeBodyPart) {
        FileSystem.zipReport();
        try {
            mimeBodyPart.attachFile(new File(FileSystem.getPathToZipReport()));
        } catch (IOException | MessagingException e) {
            Log.error("Can not attache report to the email", e);
        }
    }
}
