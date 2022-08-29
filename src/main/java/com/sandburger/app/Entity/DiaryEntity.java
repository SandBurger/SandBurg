package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class DiaryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diary_idx;


    @Column(name = "record")
    private String record;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @Builder
    public DiaryEntity(String record, UserEntity user){
        this.record = record;
        this.user = user;
    }
}
