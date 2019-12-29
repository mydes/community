package com.example.community.controller;

import com.example.community.domain.User;
import com.example.community.dto.NotificationDTO;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id")Long id,HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if (NotificationTypeEnum.REPLAY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLAY_QUESTION.getType() == notificationDTO.getType()){

            return "redirect:/question/"+notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
