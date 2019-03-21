package com.dataart.apanch.model;

public enum CategoryType {

    GAMES("GAMES"),
    EDUCATION("EDUCATION");

    String categoryType;

    private CategoryType(String categoryType){
        this.categoryType = categoryType;
    }

    public String getCategoryType(){
        return categoryType;
    }

}
