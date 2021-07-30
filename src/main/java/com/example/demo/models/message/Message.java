package com.example.demo.models.message;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;

@Document
public class Message implements Comparable<Message> {
    @Id
    private String id;
    private long dateSendMessage;
    private String message;
    private String receiver;
    private String sender;
    private String emotion;

    public Message() {
        super();
    }

    public Message(String id, long dateSendMessage, String message, String receiver, String sender, String emotion) {
        this.id = id;
        this.dateSendMessage = dateSendMessage;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.emotion = emotion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateSendMessage() {
        return dateSendMessage;
    }

    public void setDateSendMessage(long dateSendMessage) {
        this.dateSendMessage = dateSendMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    @Override
    public int compareTo(Message o) {
        return Long.compare(this.dateSendMessage,o.dateSendMessage);
    }
}
