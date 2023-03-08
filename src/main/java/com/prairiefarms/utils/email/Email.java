package com.prairiefarms.utils.email;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;
import java.util.List;

public class Email {

    private static final StringBuilder DO_NOT_REPLY = new StringBuilder()
            .append("<div style=\"float:left;width:100%;padding-bottom:2%;font-weight:bold;font-style:italic;\">")
            .append("<span>")
            .append("Please do not reply to this automated email.")
            .append("</span>")
            .append("</div>");

    private static final StringBuilder LEGAL_DISCLAIMER = new StringBuilder()
            .append("<div style=\"float:left;width:75%;padding-top:5%;\">")
            .append("<div style=\"font-size:8pt;font-weight:bold;font-style:italic;color:black;background:lightgray;\"")
            .append("<spans>")
            .append("The information transmitted in this e-mail is intended only for the addressee ")
            .append("and may contain confidential and/or privileged material.  ")
            .append("Any interception, review, retransmission, dissemination, or other use of, ")
            .append("or taking any action upon this information by persons or entities other than ")
            .append("the intended recipient is prohibited by law and may subject them to criminal ")
            .append("or civil liability.  ")
            .append("If you received this communication in error, please delete the communication ")
            .append("from your computer or network system.")
            .append("</span>")
            .append("</div>")
            .append("</div>");

    private final String server;
    private final Message message;
    private final HtmlEmail multiPartEmail;

    public Email(String server, Message message) {
        this.server = server;
        this.message = message;
        this.multiPartEmail = new HtmlEmail();
    }

    public void send() throws EmailException {
        if (StringUtils.isNotBlank(server) &&
                ObjectUtils.isNotEmpty(message.getFrom()) &&
                ObjectUtils.isNotEmpty(message.getTo()) &&
                StringUtils.isNotBlank(message.getSubject()) &&
                StringUtils.isNotBlank(message.getBody())) {
            addFromAddress(message.getFrom());
            addRecipientAddress(message.getTo());
            addCarbonCopyAddress(message.getCc());
            addAttachment(message.getAttachments());

            multiPartEmail.setHostName(StringUtils.normalizeSpace(server));
            multiPartEmail.setSubject(StringUtils.normalizeSpace(message.getSubject()));
            multiPartEmail.setHtmlMsg(StringUtils.normalizeSpace(bodyAsHtml(message.getBody())));
            multiPartEmail.send();
        }
    }

    private void addFromAddress(List<String> fromAddresses) throws EmailException {
        StringBuilder addresses = new StringBuilder();

        for (String address : fromAddresses) {
            if (StringUtils.isNotBlank(address)) {
                if (!StringUtils.normalizeSpace(addresses.toString()).equals(""))
                    addresses.append(",");
                addresses.append(StringUtils.normalizeSpace(address));
            }
        }

        multiPartEmail.setFrom(addresses.toString());
    }

    private void addRecipientAddress(List<String> recipients) throws EmailException {
        for (String recipient : recipients) {
            if (ObjectUtils.isNotEmpty(recipient))
                multiPartEmail.addTo(StringUtils.normalizeSpace(recipient));
        }
    }

    private void addCarbonCopyAddress(List<String> recipients) throws EmailException {
        for (String recipient : recipients) {
            if (ObjectUtils.isNotEmpty(recipient))
                multiPartEmail.addCc(StringUtils.normalizeSpace(recipient));
        }
    }

    private void addAttachment(List<File> files) throws EmailException {
        if (ObjectUtils.isNotEmpty(files)) {
            for (File file : files) {
                if (ObjectUtils.isNotEmpty(file))
                    multiPartEmail.attach(file);
            }
        }
    }

    private String bodyAsHtml(StringBuilder messageBody) {
        return "<body style=\"width:100%;font-family:Helvetica,Arial,sans-serif;color:#2c3e50;\">" +
                DO_NOT_REPLY +
                StringUtils.normalizeSpace(messageBody.toString()) +
                LEGAL_DISCLAIMER +
                "</body>";
    }
}
