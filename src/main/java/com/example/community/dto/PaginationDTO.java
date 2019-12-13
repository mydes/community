package com.example.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOs;
    //是否有向前按钮
    private boolean showPrevious;
    //是否是第一页按钮
    private boolean showFirstPage;
    //下一页
    private boolean showNext;
    //最后一页
    private boolean showEndPage;
    //当前页码
    private Integer page;
    //展示页码数
    private List<Integer> pages = new ArrayList<>();
    //总页数
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {
        this.page=page;
        this.totalPage=totalPage;
        pages.add(page);
        //判断展示页码数
        for (int i = 1; i<=3; i++){
            //只要不是第一页往前加页码
            if(page-i>0){
                pages.add(0,page-i);
            }
            //只要
            if(page+i <= totalPage){
                pages.add(page+i);
            }
        }

        //是否展示上一页
        showPrevious = page == 1? false:true;

        //是否展示下一页
        showNext = page == totalPage? false:true;

        //是否展示第一页
        showFirstPage = pages.contains(1)?false:true;


        //是否展示最后一页
        showEndPage = pages.contains(totalPage)?false:true;
    }
}
