package com.prairiefarms;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class Email {

	private String recipient;
	private String carbonCopy;
	private String sender;
	private String user;
	private String password;
	private String mailHost;
	private String subject;
	private String body;
	private String[] attachment;
	private String disclaimer;


	public String getRecipient () {
		return recipient;
	}

	public void setRecipient (String recipient) {
		this.recipient = recipient;
	}

	public String getCarbonCopy () {
		return carbonCopy;
	}

	public void setCarbonCopy (String carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public String getSender () {
		return sender;
	}

	public void setSender (String sender) {
		this.sender = sender;
	}

	public void setCredentials (String user, String password) {
		this.user = user;
		this.password = password;
	}

	public void setMailHost (String mailHost) {
		this.mailHost = mailHost;
	}

	public String getSubject () {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody () {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getAttachment () {
		return attachment;
	}

	public void setAttachment(String[] attachment) {
		this.attachment = attachment;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void send() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", mailHost.trim());
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(sender.trim()));
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.trim()));
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(carbonCopy.trim()));
			message.setSubject(subject.trim());

			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body.trim() + "<br><br><br>" + disclaimer.trim(), "text/html");
			multipart.addBodyPart(messageBodyPart);

			for (int i = 0; i < attachment.length; i++) {
				if (attachment[i] != null) {
					DataSource source = new FileDataSource(attachment[i].trim());

					BodyPart attachmentBodyPart = new MimeBodyPart();
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					attachmentBodyPart.setFileName(source.getName().trim());
			        multipart.addBodyPart(attachmentBodyPart);
				}
			}

			message.setContent(multipart);

			message.setSentDate(new Date());

			Transport.send(message);
		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}
}
