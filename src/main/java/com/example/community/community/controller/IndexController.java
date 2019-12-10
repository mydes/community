package com.example.community.community.controller;

import com.example.community.community.domain.Question;
import com.example.community.community.domain.User;
import com.example.community.community.mapper.QuestionMapper;
import com.example.community.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request,Model model){
        //进入网站后判断cookie中是否有登录成功后的token
        Cookie[] cookies = request.getCookies();
        //判断cookies中是否有值，避免用户删掉Cookie后登录报错
        if (cookies!=null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findBiToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        List<Question> questions = questionMapper.findAll();
        model.addAttribute("questions",questions);
        return "index";
    }
}
