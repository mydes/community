package com.example.community.mapper;

import com.example.community.domain.Comment;
import com.example.community.domain.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
    @Select("select * from question order by gmt_create desc limit #{offSet},#{size};")
    List<Question> findAll(@Param("offSet") Integer offSet,@Param("size") Integer size) ;
    @Select("select count(1) from question")
    Integer count();
    @Select("select * from question where creator = #{userId} limit #{offSet},#{size}")
    List<Question> findAllUserById(@Param("userId") Long userId,@Param("offSet") Integer offSet,@Param("size") Integer size);
    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId") Long userId);
    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Long id);
    @Update("update question set title = #{title},description = #{description},gmt_modified = #{gmtModified},tag = #{tag} where id = #{id}")
    void updatePublish(Question question);
    @Select("select view_count from question where id = #{id}")
    Integer findViewCount(Long id);
    @Update("update question set view_count = #{viewCount}+1 where id = #{id}")
    void updateViewCount(@Param("viewCount") Integer viewCount,@Param("id") Long id);
    @Select("select * from question where id = #{parentId}")
    Question findById(Long parentId);
    @Select("select comment_count from question where id = #{parentId}")
    Integer findCommentCount(Long parentId);
    @Update("update question set comment_count = #{commentCount}+1 where id = #{parentId}")
    void updateCommentCount(@Param("commentCount") Integer commentCount, @Param("parentId") Long parentId);

    @Select("SELECT * FROM question WHERE id !=#{id} and tag REGEXP #{tag}")
    List<Question> findListRelated(Question question);


}
