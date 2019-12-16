package com.example.community.enums;

import com.example.community.domain.Comment;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

     CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }
}
