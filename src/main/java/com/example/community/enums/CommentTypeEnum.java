package com.example.community.enums;

public enum CommentTypeEnum {
    //用来判断是一级评论还是二级评论
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
