package com.sumologic.report.config;

public class SearchJob {

    private String query;
    private String from;
    private String to;
    private String timezone;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "SearchJob{" +
                "query='" + query + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", timezone='" + timezone + '\'' +
                '}';
    }

}