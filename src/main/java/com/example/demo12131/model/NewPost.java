package com.example.demo12131.model;

public class NewPost {
    private String title;
    private String contents;
    NewPost(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "newPost(title : "+title+" contents : "+contents+")";
    }
}
