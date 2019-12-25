package com.example.community.service;

import com.example.community.domain.Comment;
import com.example.community.domain.User;
import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;

import java.util.List;

public interface CommentService {
    void insertComment(Comment comment, User user);

    List<CommentDTO> findListQuestionId(Long id, CommentTypeEnum type);

}
