package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long user_idx;

    @Column(name = "email")
    String email;

    @Builder
    public UserEntity(Long user_idx, String email) {
        this.user_idx = user_idx;
        this.email = email;
    }
}
