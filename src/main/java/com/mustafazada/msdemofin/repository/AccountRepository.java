package com.mustafazada.msdemofin.repository;

import com.mustafazada.msdemofin.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(Integer accountNo);
}
