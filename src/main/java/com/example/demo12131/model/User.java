package com.example.demo12131.model;


public class User {
    private String user_id;
    private String user_pwd;
    private String post_seq;
    private String user_name;

    public User() {}
    public User(String user_name, String user_id, String user_pwd) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_pwd = user_pwd;
    }

    public User(String user_id, String user_pwd) {
        this.user_id = user_id;
        this.user_pwd = user_pwd;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public void setpost_seq(String post_seq) {
        this.post_seq = post_seq;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public String getUser_pwd() {
        return this.user_pwd;
    }

    public String getpost_seq() {
        return this.post_seq;
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String toPrint() {
        return "user_id : "+getUser_id()+"user_pwd : "+getUser_pwd()+"user_name : "+getUser_name();
    }
}
