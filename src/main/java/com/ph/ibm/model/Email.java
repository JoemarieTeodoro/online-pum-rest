package com.ph.ibm.model;

import java.util.List;

public class Email {

    private String recipientType;

    private List<String> recipientAddresses;

    private String recipientAddress;

    private String senderAddress;

    private String subject;

    private String text;

    private String tempPassword;

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword( String tempPassword ) {
        this.tempPassword = tempPassword;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType( String recipientType ) {
        this.recipientType = recipientType;
    }

    public List<String> getRecipientAddresses() {
        return recipientAddresses;
    }

    public void setRecipientAddresses( List<String> recipientAddresses ) {
        this.recipientAddresses = recipientAddresses;
    }

    public void setRecipientAddress( String recipientAddress ) {
        this.recipientAddress = recipientAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress( String senderAddress ) {
        this.senderAddress = senderAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject( String subject ) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public String getRecipient() {
        return recipientAddress;
    }

}
