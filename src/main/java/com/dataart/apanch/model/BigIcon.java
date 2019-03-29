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
@Table(name="BIG_ICON_VIEW")
public class BigIcon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Lob
    @Column(name = "BIG_ICON", length = 100000)
    private byte[] icon;

    public Long getId() {
        return id;
    }

    public String getIcon() {
        if(icon == null){
            return null;
        }
        byte[] encodeBase64 = Base64.getEncoder().encode(icon);
        return new String(encodeBase64);
    }
}
