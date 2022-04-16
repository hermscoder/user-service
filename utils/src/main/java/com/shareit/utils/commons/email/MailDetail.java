package com.shareit.utils.commons.email;

public class MailDetail {

    private String name;
    private String to;
    private String from;
    private String subject;

    public MailDetail(String name, String to, String from, String subject) {
        this.name = name;
        this.to = to;
        this.from = from;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public static MailDetail.Builder newBuilder() {
        return new MailDetail.Builder();
    }

    public static class Builder {
        private String name;
        private String to;
        private String from;
        private String subject;

        private Builder(){

        }
        public MailDetail build() {
            return new MailDetail(name, to, from, subject);
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder to(String to){
            this.to = to;
            return this;
        }

        public Builder from(String from){
            this.from = from;
            return this;
        }

        public Builder subject(String subject){
            this.subject = subject;
            return this;
        }
    }
}