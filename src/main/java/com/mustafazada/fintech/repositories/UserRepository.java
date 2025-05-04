package com.mustafazada.fintech.repositories;

import com.mustafazada.fintech.entity.TechUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<TechUser, Long> {
    @Query("select p from TechUser p join fetch p.accountList where p.pin=:pin")
    Optional<TechUser> findByPin(@Param("pin") String pin);
}
