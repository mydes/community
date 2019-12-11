package com.example.community.service;

import com.example.community.dto.PaginationDTO;

public interface QuestionService {
    PaginationDTO findAll(Integer page, Integer size);

    PaginationDTO findAll(Integer userId, Integer page, Integer size);
}