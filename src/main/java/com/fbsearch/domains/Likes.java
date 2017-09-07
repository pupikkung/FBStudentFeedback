package com.fbsearch.domains;

import java.util.List;

public class Likes {
     private Summary summary;
    private List<Data> data;

    @Override
    public String toString() {
        return data.toString();
    }

    public Summary getSummary() {
        return summary;
    }

    public List<Data> getData() {
        return data;
    }
}
