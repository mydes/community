package com.example.community.service;

import com.example.community.domain.User;
import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;

public interface NotificationService {
    PaginationDTO findAll(Long id, Integer page, Integer size);

    Integer unreadCount(Long id);

    NotificationDTO read(Long id, User user);
}
