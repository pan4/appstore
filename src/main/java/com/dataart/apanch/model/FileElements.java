package com.dataart.apanch.model;

public enum FileElements {

    APP_NAME("name"),
    PACKAGE_NAME("package"),
    SMALL_ICON_NAME("picture_64"),
    BIG_ICON_NAME("picture_256");

    final String title;

    FileElements(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
