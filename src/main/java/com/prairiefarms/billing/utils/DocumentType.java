package com.prairiefarms.billing.utils;

public enum DocumentType {

    S(".pdf"),
    W(".xlsx");

    public final String fileExtension;

    DocumentType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public static DocumentType getEnumByString(String code) {
        for (DocumentType documentType : DocumentType.values()) {
            if (documentType.name().equals(code)) return documentType;
        }

        return null;
    }
}
