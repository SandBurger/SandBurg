package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long user_idx;


    @Column(name = "refresh_key")
    String refresh_key;

    @Builder
    public UserEntity(Long user_idx, String refresh_key) {
        this.user_idx = user_idx;
        this.refresh_key = refresh_key;
    }
}
