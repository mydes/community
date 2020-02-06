package com.example.community.controller;

import com.example.community.domain.User;
import com.example.community.dto.AccessTokenDTO;
import com.example.community.dto.GithubUser;
import com.example.community.provider.GithubProvider;
import com.example.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                            HttpServletRequest request,HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri("http://www.cspc2020.top/callback");
        accessTokenDTO.setState(state);
        //向github发送post请求得到token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println("???"+accessToken);
        //得到token后携带token的值访问github/user 得到用户id、名字等信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println("githubUser"+githubUser);
        //验证github用户名，验证成功后把得到的信息插入到数据库
        if(githubUser != null && githubUser.getId()!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            if (githubUser.getName()==null){
                user.setName(githubUser.getId().toString());
            }else {
                user.setName(githubUser.getName());
            }

            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //把token写入Cookie回写给浏览器
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user",githubUser);
            //成功后，用重定向
            return "redirect:/";
        }else {
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
