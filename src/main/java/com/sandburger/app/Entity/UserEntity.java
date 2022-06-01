package com.sandburger.app.Entity;

import com.sandburger.app.Util.BaseEntity;
import lombok.Getter;

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



}
