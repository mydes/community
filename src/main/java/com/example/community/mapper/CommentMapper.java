package com.example.community.mapper;

import com.example.community.domain.Comment;
import com.example.community.dto.CommentDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(id,parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values(#{id},#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insertComment(Comment comment);
    @Select("select * from where parent_id = #{parentId}")
    Comment findById(Long parentId);
    @Select("select * from comment where parent_id = #{id}")
    List<Comment> findListQuestionId(Long id);
}
