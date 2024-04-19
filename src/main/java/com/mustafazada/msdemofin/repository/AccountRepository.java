package com.mustafazada.msdemofin.repository;

import com.mustafazada.msdemofin.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AccountRepository extends JpaRepository<Account, Long> {
}
