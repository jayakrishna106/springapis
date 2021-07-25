package com.mytest.webapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(name="password_reset_token")
public class PasswordResetToken {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true,updatable = false, insertable = false)
    private User user;

    @Column(name = "expiry_dates", nullable = false)
    private Date expiryDates;

    public void setExpiryDate(int minutes){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.expiryDates = now.getTime();
    }

    public boolean isExpired(){
        return new Date().after(this.expiryDates);
    }
}
