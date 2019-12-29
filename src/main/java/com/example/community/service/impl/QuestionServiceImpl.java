package com.example.community.service.impl;

import com.example.community.domain.Question;
import com.example.community.domain.User;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.dto.QuestionQueryDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PaginationDTO findAll(String search,Integer page, Integer size) {
        if(StringUtils.isNotBlank(search)){
            //相关问题页面的mysql正则表达tag模糊查询
            String[] tags = StringUtils.split(search," ");
            String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionMapper.count(questionQueryDTO);
        if (totalCount==0){
            return null;
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
        //size*(page-1)
        Integer offSet = size * (page -1);

        //根据计算的下标和分页展示数据条数
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offSet);
        List<Question> questions = questionMapper.findAll(questionQueryDTO);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setData(questionDTOS);

        return paginationDTO;
    }

    @Override
    public PaginationDTO findAll(Long userId, Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        if (totalCount==0){
            return null;
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
        List<Question> questions = questionMapper.findAllUserById(userId,offSet,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setData(questionDTOS);

        return paginationDTO;
    }

    @Override
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.getById(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.create(question);
        }else {
            question.setGmtModified(question.getGmtCreate());
            questionMapper.updatePublish(question);
        }
    }

    @Override
    public void invView(Long id) {
        Integer viewCount = questionMapper.findViewCount(id);
        questionMapper.updateViewCount(viewCount,id);
    }

    @Override
    public List<QuestionDTO> findListRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        //相关问题页面的mysql正则表达tag模糊查询

        String[] tags = StringUtils.split(queryDTO.getTag(),",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        //给转义字符
        if (regexpTag.contains("c++")){
            regexpTag = regexpTag.replace("c++", "c\\++");
        }
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionMapper.findListRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
