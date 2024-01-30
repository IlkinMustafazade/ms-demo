package com.mustafazada.tech_app_3.repository;

import com.mustafazada.tech_app_3.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
