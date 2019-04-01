package com.dataart.apanch.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "APP_PACKAGE")
public class AppPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Lob
    @NotEmpty
    @Column(name = "FILE", nullable = false, length = 100000)
    private byte[] file;

    @Lob
    @Column(name = "SMALL_ICON", length = 100000)
    private byte[] smallIcon;

    @Lob
    @Column(name = "BIG_ICON", length = 100000)
    private byte[] bigIcon;

    @NotEmpty
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "SMALL_ICON_NAME")
    private String smallIconName;

    @Column(name = "BIG_ICON_NAME")
    private String bigIconName;

    @OneToOne(mappedBy = "appPackage", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private App app;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(byte[] smallIcon) {
        this.smallIcon = smallIcon;
    }

    public byte[] getBigIcon() {
        return bigIcon;
    }

    public void setBigIcon(byte[] bigIcon) {
        this.bigIcon = bigIcon;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSmallIconName() {
        return smallIconName;
    }

    public void setSmallIconName(String smallIconName) {
        this.smallIconName = smallIconName;
    }

    public String getBigIconName() {
        return bigIconName;
    }

    public void setBigIconName(String bigIconName) {
        this.bigIconName = bigIconName;
    }
}
