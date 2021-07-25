package com.mytest.webapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_login_date")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class UserLoginDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login_date")
    Timestamp loginDate;

    @Column(name = "successful_login")
    boolean successfulLogin;

    @Column(name = "ip_user")
    String ipUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;
}
