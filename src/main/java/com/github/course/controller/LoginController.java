package com.github.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LoginController {
    private Map<String, String> user = new ConcurrentHashMap<>();
    private Map<String, String> userToCookie = new ConcurrentHashMap<>();

    {
        user.put("test1", "test1");
        user.put("test2", "test2");
    }

    public static class UsernameAndPassword {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody UsernameAndPassword usernameAndPassword, HttpServletResponse response) {
        // 检验用户的登陆名和密码是否正确
        // 如果正确，允许用户登陆，并向用户发送一段 Cookie
        // 下次用户带着 Cookie 访问就无须重复登陆
        // Cookie和用户信息在服务端存储（Session）
        String username = usernameAndPassword.getUsername();
        String password = usernameAndPassword.getPassword();
        if (user.get(username).equals(password)) {
            // 登陆成功
            String courseSessionId = UUID.randomUUID().toString();
            String cookieName = "courseSessionId";
            response.addCookie(new Cookie(cookieName, courseSessionId));
            userToCookie.put(username, courseSessionId);
            return "Login Success";
        } else {
            return "Login fail";
        }

    }
}
