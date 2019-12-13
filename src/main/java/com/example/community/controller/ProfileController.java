package com.example.community.controller;

import com.example.community.domain.User;
import com.example.community.dto.PaginationDTO;
import com.example.community.mapper.UserMapper;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action")String action,Model model,HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        //根据用户id查询当前用户的发帖数及分页
        PaginationDTO paginationDTO = questionService.findAll(user.getId(), page, size);
        if (paginationDTO==null){
            model.addAttribute("noProblem","你没有提问哦");
        }else {
            model.addAttribute("paginationDTO",paginationDTO);
        }
        return "profile";
    }
}
