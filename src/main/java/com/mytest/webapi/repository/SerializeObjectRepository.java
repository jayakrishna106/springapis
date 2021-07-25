package com.mytest.webapi.repository;

import com.mytest.webapi.model.HashObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SerializeObjectRepository extends JpaRepository<HashObject, UUID> {
}
