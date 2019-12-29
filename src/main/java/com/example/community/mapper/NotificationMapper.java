package com.example.community.mapper;

import com.example.community.domain.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(id,notifier,receiver,outerId,type,gmt_create,status,notifier_name,outer_title) values(#{id},#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void insertNotification(Notification notification);
    @Select("select count(*) from notification where receiver = #{userId} and status = #{status}")
    Integer countByIdUnread(@Param("userId") Long userId, @Param("status") int status);
    @Select("select * from notification where receiver = #{userId} order by gmt_create desc limit #{offSet},#{size}")
    List<Notification> findAllUserById(@Param("userId") Long userId, @Param("offSet") Integer offSet,@Param("size") Integer size);
    @Select("select * from notification where id = #{id}")
    Notification findById(Long id);
    @Update("update notification set status = #{status} where id = #{id}")
    void updateStatus(Notification notification);
    @Select("select count(*) from notification where receiver = #{userId}")
    Integer countByUserId(@Param("userId") Long userId);
}
