package com.fbsearch.domains;

public class Data {

    private String id;
    private String message;
    private String updated_time;
    private Paging paging;

    @Override
    public String toString() {
        return "Data{" + "id=" + id + '}';
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUpdated_time() {
        return updated_time;
    }


    public Paging getPaging() {
        return paging;
    }
}
