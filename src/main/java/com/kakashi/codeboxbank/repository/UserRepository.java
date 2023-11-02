package com.kakashi.codeboxbank.repository;

import com.kakashi.codeboxbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
