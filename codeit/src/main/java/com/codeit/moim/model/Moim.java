package com.codeit.moim.model;

import com.codeit.review.model.Review;
import com.codeit.util.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Moim extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "moim_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MoimType moimType;

    @Column(name = "recruitment_deadline", nullable = false)
    private LocalDateTime recruitmentDeadline;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "min_participants", nullable = false)
    private Integer minParticipants;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "moim_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MoimStatus moimStatus;

    @OneToOne(mappedBy = "moim", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MoimLocation moimLocation;

    @OneToMany(mappedBy = "moim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MoimPhoto> moimPhotos;

    @Builder
    public Moim(String title, String content, MoimType moimType, LocalDateTime recruitmentDeadline,
                LocalDateTime startDate,
                LocalDateTime endDate, Integer minParticipants, Integer maxParticipants, MoimStatus moimStatus,
                MoimLocation moimLocation, List<MoimPhoto> moimPhotos) {
        this.title = title;
        this.content = content;
        this.moimType = moimType;
        this.recruitmentDeadline = recruitmentDeadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.moimStatus = moimStatus;
        this.moimLocation = moimLocation;
        this.moimPhotos = moimPhotos;
    }
}
