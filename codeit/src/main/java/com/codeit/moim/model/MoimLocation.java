package com.codeit.moim.model;

import com.codeit.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimLocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "si", nullable = false)
    private String si;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @OneToOne
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @Builder
    public MoimLocation(String si, String district, String roadAddress, Moim moim) {
        this.si = si;
        this.district = district;
        this.roadAddress = roadAddress;
        this.moim = moim;
    }
}
