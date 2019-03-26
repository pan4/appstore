package com.dataart.apanch.model;

public enum CategoryType {

    GAMES("GAMES"),
    EDUCATION("EDUCATION");

    String categoryType;

    CategoryType(String categoryType){
        this.categoryType = categoryType;
    }

    public String getCategoryType(){
        return categoryType;
    }

}
