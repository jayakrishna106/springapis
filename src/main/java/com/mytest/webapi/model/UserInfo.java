package com.mytest.webapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private byte[] name;

    @Column(name="phone")
    @NotNull
    private byte[] phone;

    @Column(name="age")
    @NotNull
    private byte[] age;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = true, updatable = false, insertable = false)
    private User user;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
