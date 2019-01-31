package com.appdevgenie.fcmmessenger.Models;

public class Message {

    private String body;
    private String from;
    private String to;
    private long timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;

    public Message() {
    }

    public Message(String body, String from, String to, long timestamp, long negatedTimestamp, long dayTimestamp) {
        this.body = body;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
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

    public long getNegatedTimestamp() {
        return negatedTimestamp;
    }

    public void setNegatedTimestamp(long negatedTimestamp) {
        this.negatedTimestamp = negatedTimestamp;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(long dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", timestamp=" + timestamp +
                ", negatedTimestamp=" + negatedTimestamp +
                ", dayTimestamp=" + dayTimestamp +
                '}';
    }
}
