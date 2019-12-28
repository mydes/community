package com.example.community.controller;

import com.example.community.domain.Question;
import com.example.community.domain.User;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
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
    private QuestionService questionService;
    @GetMapping("/")
    public String index(    Model model,
                            @RequestParam(name = "page",defaultValue = "1") Integer page,
                            @RequestParam(name = "size",defaultValue = "5") Integer size,
                            @RequestParam(name = "search",required = false) String search
                        ){
        if (StringUtils.isBlank(search)){
            search = null;
        }
        PaginationDTO pagination = questionService.findAll(search,page,size);
        if (pagination!=null){
            model.addAttribute("pagination",pagination);
            model.addAttribute("search",search);
        }else {
            model.addAttribute("noProblem","当前还没有人提问");
        }
        return "index";
    }
}
