package com.prairiefarms.utils.email;

import java.io.File;
import java.util.List;

public class Message {

    private final List<String> from;
    private final List<String> to;
    private final List<String> cc;
    private final String subject;
    private final StringBuilder body;
    private final List<File> attachments;

    public Message(List<String> from,
                   List<String> to,
                   List<String> cc,
                   String subject,
                   StringBuilder body,
                   List<File> attachments) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }

    public List<String> getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public String getSubject() {
        return subject;
    }

    public StringBuilder getBody() {
        return body;
    }

    public List<File> getAttachments() {
        return attachments;
    }
}
