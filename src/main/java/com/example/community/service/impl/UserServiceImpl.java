package com.example.community.service.impl;

import com.example.community.domain.User;
import com.example.community.mapper.UserMapper;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public void createOrUpdate(User user) {
        //判断数据库是否有用户信息
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        //没有插入
        if (dbUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {//有，更新github上获取的数据
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.updateUser(dbUser);
        }
    }
}
