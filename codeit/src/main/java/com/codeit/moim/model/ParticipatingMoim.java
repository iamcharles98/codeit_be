package com.codeit.moim.model;

import com.codeit.user.model.User;
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
@Table(name = "participating_moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ParticipatingMoim extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id", nullable = false)
    private Moim moim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_host",nullable = false)
    private boolean isHost;

    @Builder
    public ParticipatingMoim(Moim moim, User user, boolean isHost) {
        this.moim = moim;
        this.user = user;
        this.isHost = isHost;
    }
}
