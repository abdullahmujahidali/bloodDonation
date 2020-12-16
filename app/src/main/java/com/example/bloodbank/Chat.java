package com.example.bloodbank;

public class Chat {
    private String sender;
    private String receiver;
    private String Message;

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    private boolean isseen;

    public Chat(String sender, String receiver, String message,boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.Message = message;
        this.isseen=isseen;
    }

    public Chat() {
    }


    public String getSender() {

        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
}

