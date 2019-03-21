package com.dataart.apanch.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="APP")
public class App {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name="NAME", nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID", nullable=false)
    private Category category;

    @NotEmpty
    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="DOWNLOADS_COUNT", nullable=false)
    private Integer downloadsCount;

    @NotEmpty
    @Column(name="PACKAGE", nullable=false)
    private String packageName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(Integer downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
