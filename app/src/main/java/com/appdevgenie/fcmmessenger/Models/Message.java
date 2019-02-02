package com.appdevgenie.fcmmessenger.Models;

public class Message {

    private String body;
    private String from;
    private String to;
    private long timestamp;

    public Message() {
    }

    public Message(String body, String from, String to, long timestamp) {
        this.body = body;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
