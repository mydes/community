package com.example.community.community.controller;

import com.example.community.community.domain.Question;
import com.example.community.community.domain.User;
import com.example.community.community.mapper.QuestionMapper;
import com.example.community.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false) String title,
                            @RequestParam(value = "description",required = false) String description,
                            @RequestParam(value = "tag",required = false) String tag,
                            HttpServletRequest request, Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title==null || title.equals("")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null || description.equals("")){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag==null || tag.equals("")){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        if (request.getSession().getAttribute("user")==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        User user = null;
        //进入网站后判断cookie中是否有登录成功后的token
        Cookie[] cookies = request.getCookies();
        //判断cookies中是否有值，避免用户删掉Cookie后登录报错
        if (cookies!=null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findBiToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
