package com.example.community.service;

import com.example.community.domain.Question;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {
    PaginationDTO findAll(Integer page, Integer size);

    PaginationDTO findAll(Long userId, Integer page, Integer size);

    QuestionDTO getById(Long id);

    void createOrUpdate(Question question);

    void invView(Long id);

    List<QuestionDTO> findListRelated(QuestionDTO questionDTO);
}
