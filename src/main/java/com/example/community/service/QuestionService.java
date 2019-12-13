package com.example.community.service;

import com.example.community.domain.Question;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;

public interface QuestionService {
    PaginationDTO findAll(Integer page, Integer size);

    PaginationDTO findAll(Integer userId, Integer page, Integer size);

    QuestionDTO getById(Integer id);

    void createOrUpdate(Question question);
}
