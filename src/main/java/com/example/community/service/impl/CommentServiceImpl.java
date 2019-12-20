package com.example.community.service.impl;

import com.example.community.domain.Comment;
import com.example.community.domain.Question;
import com.example.community.domain.User;
import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.CommentMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.service.CommentService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void insertComment(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //二级回复
            Comment dbComment = commentMapper.findById(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insertComment(comment);
            //二级回复数
            Integer commentCount = commentMapper.findCommentCount(comment.getParentId());
            commentMapper.updateCommentCount(commentCount,comment.getParentId());
        }else {
            //一级回复
            Question question = questionMapper.findById(comment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertComment(comment);
            Integer commentCount = questionMapper.findCommentCount(comment.getParentId());
            questionMapper.updateCommentCount(commentCount,comment.getParentId());
        }
    }
    //重點，重點，重點，重點，重點，重點，重點，重點
    @Override
    public List<CommentDTO> findListQuestionId(Long id, CommentTypeEnum type) {
        List<Comment> comments =   comments = commentMapper.findListQuestionId(id,type.getType());
        //遍历查询到的回復，取回復者id把他們存到set集合（set集合不能有重複，起到過濾作用）
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //轉換成list
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //獲取評論人并轉換爲 Map
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userMapper.findById(userId);
            users.add(user);
        }
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //轉換 comment 為 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
