package com.example.demo12131.controller;

import com.example.demo12131.dao.PostDAO;
import com.example.demo12131.encryption.Encryption;
import com.example.demo12131.model.NewPost;
import com.example.demo12131.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class MainController {
    PostDAO pd = new PostDAO();

    // 시작화면
    @GetMapping("")
    public String main(Model model, @CookieValue(name = "user_name", required = false) Cookie name) {
        model.addAttribute("data", pd.selectPostData());

        if(name != null)
            model.addAttribute("name", name.getValue());;

        return "main";
    }

    // 등록페이지
    @GetMapping("newPost")
    public String newPost(@CookieValue(name = "user_id", required = false) Cookie user_id, Model model) {
        if(Objects.equals(user_id.getValue(), "")) {
            model.addAttribute("login", "N");
            return "tempPage";
        } else
            return "newPost";
    }

    @PostMapping("newPost")
    public String newPost(NewPost form, @CookieValue(name = "user_id") Cookie user_id) throws Exception {
        pd.insertPostData(form.getTitle(), form.getContents(), Encryption.aesCBCDecode(user_id.getValue()));
        return "tempPage";
    }
    // 등록페이지


    // 수정페이지
    @GetMapping("detailPage/{post_seq}")
    public String detailPost(@PathVariable("post_seq") String post_seq,  Model model) throws Exception {
        model.addAttribute("data", pd.selectDetailPostsData(post_seq));
        model.addAttribute("chat_data", pd.selectChatData(post_seq));
        return "detailPost";
    }

    @GetMapping("likes/{post_seq}")
    public String likes(@PathVariable("post_seq") int post_seq, @CookieValue(name = "user_id", required = false) Cookie user_id, Model model) throws Exception {
        if(user_id == null || Objects.equals(user_id.getValue(), "")) {
            model.addAttribute("login", "N");
            return "tempPage";
        } else {
            String likes_yn = "N";
            if(pd.selectUserLikesChk(Encryption.aesCBCDecode(user_id.getValue()), post_seq)) {
                likes_yn = "Y";
            } else {
                pd.updateLikesData(Encryption.aesCBCDecode(user_id.getValue()), post_seq);
            }

            model.addAttribute("likes_yn", likes_yn);
            model.addAttribute("user_id", Encryption.aesCBCDecode(user_id.getValue()));
            model.addAttribute("post_seq", post_seq);
            return "likes";
        }
    }

    @GetMapping("likes/{post_seq}/{chat_seq}")
    public String chatLikes(@PathVariable("post_seq") int post_seq, @PathVariable("chat_seq") int chat_seq, @CookieValue(name = "user_id", required = false) Cookie user_id, Model model) throws Exception {
        if(user_id == null || Objects.equals(user_id.getValue(), "")) {
            model.addAttribute("login", "N");
            return "tempPage";
        } else {
            String likes_yn = "N";
            if(pd.selectUserLikesChk(Encryption.aesCBCDecode(user_id.getValue()), post_seq, chat_seq)) {
                likes_yn = "Y";
            } else {
                pd.updateLikesData(Encryption.aesCBCDecode(user_id.getValue()), post_seq, chat_seq);
            }

            model.addAttribute("likes_yn", likes_yn);
            model.addAttribute("post_seq", post_seq);
            return "likes";
        }
    }

    @GetMapping("add_chat/{user_id}/{post_seq}/{chat_contents}")
    public String chat(@PathVariable("user_id") String user_id, @PathVariable("post_seq") int post_seq, @PathVariable("chat_contents") String chat_contents, @CookieValue(name = "user_id", required = false) Cookie chat_user_id, Model model) throws Exception {
        if(user_id == null || Objects.equals(chat_user_id.getValue(), "")) {
            model.addAttribute("login", "N");
            return "tempPage";
        } else {
            pd.insertChatData(user_id, post_seq, chat_contents, Encryption.aesCBCDecode(chat_user_id.getValue()));
            model.addAttribute("post_seq", post_seq);
            return "likes";
        }
    }
    // 수정페이지


    // 로그인페이지
    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    public String chk_login(User form, Model model, HttpServletResponse response) throws Exception {
        String name = pd.selectUserLoginData(form.getUser_id(), form.getUser_pwd());
        model.addAttribute("name", name);
        if(Objects.equals(name, "0000000000000000000000000")) {
            return "login";
        } else {
            Cookie cookie = new Cookie("user_name", name);
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
            response.addCookie(cookie);

            cookie = new Cookie("user_id", Encryption.aesCBCEncode(form.getUser_id()));
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
            response.addCookie(cookie);

            return "tempPage";
        }
    }
    // 로그인페이지

    @GetMapping("logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("user_name", null);
        cookie.setMaxAge(0);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);

        cookie = new Cookie("user_id", null);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(cookie);

        return "tempPage";
    }

    // 회원가입페이지
    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("user_name", "");
        return "register";
    }

    @PostMapping("register")
    public String user_reg(User form, Model model) {
        System.out.println(form.toPrint());
        if(pd.selectUserIdOverlap(form.getUser_id())) {
            model.addAttribute("name", form.getUser_name());
            return "register";
        } else {
            pd.insertUserInfoData(form.getUser_name(), form.getUser_id(), form.getUser_pwd());

            return "register_success";
        }
    }
    // 회원가입페이지
}
