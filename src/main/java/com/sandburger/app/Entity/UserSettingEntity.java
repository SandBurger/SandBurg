package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_setting")
public class UserSettingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long setting_idx;

    @Column(name = "push_time")
    LocalDateTime push_time;
}
