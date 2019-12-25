package com.example.community.service.impl;

import com.example.community.domain.Notification;
import com.example.community.domain.User;
import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.NotificationMapper;
import com.example.community.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationMapper notificationMapper;
    @Override
    public PaginationDTO findAll(Long userId, Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = notificationMapper.countByUserId(userId, NotificationStatusEnum.UNREAD.getStatus());
        if (totalCount==0){
            return new PaginationDTO();
        }
        if(totalCount % size==0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size+1;
        }
        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        //size*(page-1)（数据库为空的时候计算的是个负数）
        Integer offSet = size * (page -1);

        //根据计算的下标和分页展示数据条数
        List<Notification> notifications = notificationMapper.findAllUserById(userId,offSet,size);
        if (notifications.size()==0){
            return paginationDTO;
        }
        List notificationDTOS = new ArrayList();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);

        return paginationDTO;
    }

    @Override
    public Integer unreadCount(Long id) {
        return notificationMapper.countByUserId(id,NotificationStatusEnum.UNREAD.getStatus());
    }

    @Override
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.findById(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateStatus(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }


}
