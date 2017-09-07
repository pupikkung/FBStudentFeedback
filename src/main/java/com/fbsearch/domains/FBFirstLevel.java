package com.fbsearch.domains;

public class FBFirstLevel {

    private String id;
    private String name;
    private String description;
    private Feed feed;

    @Override
    public String toString() {
        return "FBTest{" + "id=" + id + ", name=" + name + ", description=" + description + ", feed=" + feed.toString() + '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

}
