package com.codeit.moim.model;

import com.codeit.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimPhoto extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_photo_id")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "photo_name")
    private String photoName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id", nullable = false)
    private Moim moim;

    @Builder
    public MoimPhoto(String url, String photoName, Moim moim) {
        this.url = url;
        this.photoName = photoName;
        this.moim = moim;
    }
}
