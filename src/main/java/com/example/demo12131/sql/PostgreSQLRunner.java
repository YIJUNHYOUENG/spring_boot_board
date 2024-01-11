package com.example.demo12131.sql;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;


public class PostgreSQLRunner {
    private String URL = "jdbc:postgresql://localhost:5432/toyProject";
    private String USERNAME = "postgres"; //postgresql 계정
    private String PASSWORD = "jh9171w!!"; //비밀번호

    public JSONObject selectPostsData() {
        JSONObject row = new JSONObject();
        try {
            int idx = 0;
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select a.title, a.likes, a.user_id, a.post_seq, a.reg_time, b.user_name from posts a\n" +
                                                "left join user_info b on a.user_id = b.user_id order by a.post_seq");
            while(rs.next()) {
                JSONObject obj = new JSONObject();
                ResultSetMetaData rsmd = rs.getMetaData();
                int total_rows = rsmd.getColumnCount(); // MVC
                for (int i = 0; i < total_rows; i++) {
                    String columnName = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    obj.put(columnName, columnValue);
                }
                row.put(String.valueOf(idx), obj);
                idx += 1;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return row;
    }

    public JSONObject selectDetailPostsData(String post_seq) {
        JSONObject obj = new JSONObject();
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select a.title, a.contents, a.likes, a.user_id, a.post_seq, a.reg_time, b.user_name from posts a\n" +
                                                "left join user_info b on a.user_id = b.user_id where a.post_seq = "+post_seq);
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            int total_rows = rsmd.getColumnCount(); // MVC
            for (int i = 0; i < total_rows; i++) {
                String columnName = rsmd.getColumnLabel(i + 1);
                Object columnValue = rs.getObject(i + 1);
                obj.put(columnName, columnValue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void insertPostsData(String title, String contents, String user_id) {


        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre = con.prepareStatement("insert into posts values(?, ?, ?, (select coalesce(max(post_seq)+1, 0) from posts), to_char(now(),'YYYYMMDD'), 0)");
            pre.setString(1, title);
            pre.setString(2, contents);
            pre.setString(3, user_id);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 게시물 좋아요 클릭
    public void updateLikesData(int post_seq) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre = con.prepareStatement("update posts set likes = likes+1 where post_seq = ?");
            pre.setInt(1, post_seq);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 댓글 좋아요 클릭
    public void updateLikesData(int postSeq, int chatSeq) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre = con.prepareStatement("update chat set likes = likes+1 where post_seq = ? and chat_seq = ?");
            pre.setInt(1, postSeq);
            pre.setInt(2, chatSeq);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertChatData(String userId, int postSeq, String chatContents, String chatUserId) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre;
            pre = con.prepareStatement("insert into chat values(?, ?, ?, ?, 0, (select coalesce(max(chat_seq)+1, 0) from chat where post_seq = ?), to_char(now(),'YYYYMMDD'))");
            pre.setString(1, userId);
            pre.setInt(2, postSeq);
            pre.setString(3, chatContents);
            pre.setString(4, chatUserId);
            pre.setInt(5, postSeq);

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSONObject selectChatData(String postSeq) {
        JSONObject row = new JSONObject();
        try {
            int idx = 0;
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select a.*, b.user_name from chat a left join user_info b on a.chat_user_id = b.user_id\n" +
                                                "where a.post_seq = "+postSeq+" order by a.chat_seq");
            while(rs.next()) {
                JSONObject obj = new JSONObject();
                ResultSetMetaData rsmd = rs.getMetaData();
                int total_rows = rsmd.getColumnCount(); // MVC
                for (int i = 0; i < total_rows; i++) {
                    String columnName = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    obj.put(columnName, columnValue);
                }
                row.put(String.valueOf(idx), obj);
                idx += 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    public boolean selectUserIdOverlap(String userId) {
        boolean id_chk = false;
        try {
            int idx = 0;
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select * from user_info where user_id = '"+userId+"'");
            if(rs.next()) id_chk = true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id_chk;
    }

    public void insertUserInfoData(String userName, String userId, String userPwd) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre;
            pre = con.prepareStatement("insert into user_info values(?, ?, ?, 'N', to_char(now(),'YYYYMMDD'))");
            pre.setString(1, userId);
            pre.setString(2, userPwd);
            pre.setString(3, userName);

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String selectUserLoginData(String userId, String userPwd) {
        String user_name = "0000000000000000000000000";
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select user_name from user_info where user_id = '"+userId+"' and user_pwd = '"+userPwd+"'");
            if(rs.next()) user_name = (String)rs.getObject(1);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return user_name;
    }

    public void insertUserLikesData(String userId, int postSeq) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre;
            pre = con.prepareStatement("insert into user_likes values(?, ?, null,'1')");
            pre.setString(1, userId);
            pre.setInt(2, postSeq);

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUserLikesData(String userId, int postSeq, int chatSeq) {
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            System.out.println(con); //연결 정보 출력
            PreparedStatement pre;
            pre = con.prepareStatement("insert into user_likes values(?, ?, ?, '2')");
            pre.setString(1, userId);
            pre.setInt(2, postSeq);
            pre.setInt(3, chatSeq);

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean selectUserLikesChk(String userId, int postSeq) {
        boolean likes_yn = false;
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select * from user_likes where user_id = '"+userId+"' and post_seq = "+postSeq+" and gubun='"+1+"'");
            if(rs.next()) likes_yn = true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return likes_yn;
    }

    public boolean selectUserLikesChk(String userId, int postSeq, int chatSeq) {
        boolean likes_yn = false;
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); //db 연결
            Statement pre = con.createStatement();
            ResultSet rs = pre.executeQuery("select * from user_likes where user_id = '"+userId+"' and post_seq = "+postSeq +" and chat_seq = "+chatSeq+" and gubun='"+2+"'");
            if(rs.next()) likes_yn = true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return likes_yn;
    }
}