package com.example.demo12131.dao;

import com.example.demo12131.sql.PostgreSQLRunner;
import org.json.JSONObject;

public class PostDAO {
    PostgreSQLRunner p = new PostgreSQLRunner();
    private String userId;
    private String userPwd;

    public JSONObject selectPostData() {
        JSONObject json = p.selectPostsData();
        System.out.println("json 객체 "+json);

        return json;
    }

    public void insertPostData(String title, String message, String user_id) {
        p.insertPostsData(title, message, user_id);
    }

    public JSONObject selectDetailPostsData(String postSeq) {
        JSONObject json = p.selectDetailPostsData(postSeq);
        System.out.println("json 객체 "+json);

        return json;
    }

    public void updateLikesData(String userId, int postSeq) {
        p.insertUserLikesData(userId, postSeq);
        p.updateLikesData(postSeq);
    }

    public void insertChatData(String userId, int postSeq, String chatContents, String ChatUserId) {
        p.insertChatData(userId, postSeq, chatContents, ChatUserId);
    }

    public JSONObject selectChatData(String postSeq) {
        JSONObject json = p.selectChatData(postSeq);
        System.out.println("json 객체 "+json);

        return json;
    }

    public void updateLikesData(String userId, int postSeq, int chatSeq) {
        p.insertUserLikesData(userId, postSeq, chatSeq);
        p.updateLikesData(postSeq, chatSeq);
    }

    public boolean selectUserIdOverlap(String userId) {
        return p.selectUserIdOverlap(userId);
    }

    public void insertUserInfoData(String userName, String userId, String userPwd) {
        p.insertUserInfoData(userName, userId, userPwd);
    }

    public String selectUserLoginData(String userId, String userPwd) {
        return p.selectUserLoginData(userId, userPwd);
    }

    public boolean selectUserLikesChk(String userId, int postSeq) {
        return p.selectUserLikesChk(userId, postSeq);
    }

    public boolean selectUserLikesChk(String userId, int postSeq, int chatSeq) {
        return p.selectUserLikesChk(userId, postSeq, chatSeq);
    }
}
