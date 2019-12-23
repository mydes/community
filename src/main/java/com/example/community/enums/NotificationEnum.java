package com.example.community.enums;

public enum NotificationEnum {
    REPLAY_QUESTION(1,"回复了问题"),
    REPLAY_COMMENT(2,"回复了评论");
    private int status;
    private String name;
    NotificationEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
