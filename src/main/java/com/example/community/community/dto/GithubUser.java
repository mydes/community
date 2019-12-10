package com.example.community.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    //    参数封装
    private Long id;
    private String name;
    private String bio;
    private String avatar_url;

}
