package com.example.community.service;

import com.example.community.domain.Comment;
import com.example.community.dto.CommentDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.enums.CommentTypeEnum;

import java.util.List;

public interface CommentService {
    void insertComment(Comment comment);

    List<CommentDTO> findListQuestionId(Long id, CommentTypeEnum type);

}
