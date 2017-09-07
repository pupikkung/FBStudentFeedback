package com.fbsearch.domains;

import java.util.List;

public class Feed {

    private List<Data> data;
    private Paging paging;

    public List<Data> getData() {
        return data;
    }

    public Paging getPaging() {
        return paging;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
