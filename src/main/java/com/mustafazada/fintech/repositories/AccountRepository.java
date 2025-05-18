package com.mustafazada.fintech.repositories;

import com.mustafazada.fintech.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(Integer accountNumber);
}
