package com.example.community.controller;

import com.example.community.domain.Comment;
import com.example.community.domain.User;
import com.example.community.dto.CommentCreateDTO;
import com.example.community.dto.ResultDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @RequestMapping(value = "/comment",method =RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(20l);
        comment.setLikeCount(0);
        commentService.insertComment(comment);
        return ResultDTO.okOf();
    }
}
