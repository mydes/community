package com.example.community.service.impl;

import com.example.community.domain.Question;
import com.example.community.domain.User;
import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PaginationDTO findAll(Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
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
        List<Question> questions = questionMapper.findAll(offSet,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestionDTOs(questionDTOS);

        return paginationDTO;
    }

    @Override
    public PaginationDTO findAll(Integer userId, Integer page, Integer size) {
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
        paginationDTO.setQuestionDTOs(questionDTOS);

        return paginationDTO;
    }

    @Override
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
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
            questionMapper.create(question);
        }else {
            question.setGmtModified(question.getGmtCreate());
            questionMapper.updatePublish(question);
        }
    }
}
