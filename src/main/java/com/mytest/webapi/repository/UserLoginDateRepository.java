package com.mytest.webapi.repository;

import com.mytest.webapi.model.User;
import com.mytest.webapi.model.UserLoginDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLoginDateRepository extends JpaRepository<UserLoginDate, Long> {

    List<UserLoginDate> findByUserOrderByLoginDateDesc(User user, Pageable pageable);
}
