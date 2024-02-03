package com.mustafazada.tech_app_3.repository;

import com.mustafazada.tech_app_3.entity.TechUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TechUser, Long> {
    @Query(value = "select p from TechUser p join fetch p.accountList where p.pin = :pin")
    Optional<TechUser> findByPin(String pin);
}
