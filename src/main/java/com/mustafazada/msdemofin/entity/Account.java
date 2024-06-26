package com.mustafazada.msdemofin.entity;

import com.mustafazada.msdemofin.util.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "balance")
    BigDecimal balance;
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    Currency currency;
    @Column(name = "status")
    Boolean isActive;
    @Column(name = "account_no", unique = true)
    Integer accountNo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    TechUser user;
}
