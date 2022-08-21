package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class DiaryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long diary_idx;


    @Column(name = "record")
    String record;

    @Column(name = "sequence")
    Integer sequence;

    @Builder
    public DiaryEntity(String record, Integer sequence){
        this.record = record;
        this.sequence = sequence;
    }
}
