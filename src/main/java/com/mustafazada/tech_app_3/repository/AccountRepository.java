package com.mustafazada.tech_app_3.repository;

import com.mustafazada.tech_app_3.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(Integer accountNo);
}
