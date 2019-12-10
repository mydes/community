package com.example.community.community.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    //    参数封装
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
