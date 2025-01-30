package com.codeit.moim.model;

import com.codeit.util.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
    private LocalDate recruitmentDeadline;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "min_participants", nullable = false)
    private Integer minParticipants;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "moim_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MoimStatus moimStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "moim_id")
    private MoimLocation moimLocation;

    @OneToMany(mappedBy = "moim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MoimPhoto> moimPhotos;

    @Builder
    public Moim(String title, String content, MoimType moimType, LocalDate recruitmentDeadline,
                LocalDate startDate,
                LocalDate endDate, Integer minParticipants, Integer maxParticipants, MoimStatus moimStatus,
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

    public void addPhoto(MoimPhoto photo) {
        this.moimPhotos.add(photo);
    }
}
