package com.example.community.service;

import com.example.community.domain.User;
import org.springframework.stereotype.Service;

public interface UserService {

    void createOrUpdate(User user);
}
