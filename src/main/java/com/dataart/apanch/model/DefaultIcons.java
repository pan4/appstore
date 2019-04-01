package com.dataart.apanch.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Base64;

@Entity
@Immutable
@Table(name = "DEFAULT_ICONS")
public class DefaultIcons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "SMALL_ICON", length = 100000)
    private byte[] smallIcon;

    @Lob
    @Column(name = "BIG_ICON", length = 100000)
    private byte[] bigIcon;

    public String getSmallIcon() {
        if (smallIcon == null) {
            return null;
        }
        byte[] encodeBase64 = Base64.getEncoder().encode(smallIcon);
        return new String(encodeBase64);
    }

    public String getBigIcon() {
        if (bigIcon == null) {
            return null;
        }
        byte[] encodeBase64 = Base64.getEncoder().encode(bigIcon);
        return new String(encodeBase64);
    }
}