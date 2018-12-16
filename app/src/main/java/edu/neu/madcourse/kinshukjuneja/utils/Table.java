package edu.neu.madcourse.kinshukjuneja.utils;

public enum Table {

    USERNAME("username"), SCORE("score"), HUSERDETAIL("userdetail"), HFRIEND("friend");

    private String tableName;

    Table(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

}
