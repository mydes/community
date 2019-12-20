package com.example.community.mapper;

import com.example.community.domain.Comment;
import com.example.community.dto.CommentDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(id,parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values(#{id},#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insertComment(Comment comment);
    @Select("select * from comment where id = #{parentId}")
    Comment findById(Long parentId);
    @Select("select * from comment where parent_id = #{id} and type = #{type} order by gmt_create desc")
    List<Comment> findListQuestionId(@Param("id") Long id,@Param("type") Integer type);
    @Select("select * from comment where id = #{id} and type = #{type} order by gmt_create desc")
    List<Comment> findListCommentId(@Param("id")Long id, @Param("type")Integer type);
    @Select("select comment_count from comment where id = #{parentId}")
    Integer findCommentCount(Long parentId);
    @Update("update comment set comment_count = #{commentCount}+1 where id = #{parentId}")
    void updateCommentCount(@Param("commentCount") Integer commentCount, @Param("parentId") Long parentId);
}
