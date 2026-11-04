package com.example.vk_currency_bot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_history")
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHistory {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vk_user_id", nullable = false)
    private Long vkUserId;

    @Column(name = "request_text", nullable = false)
    private String requestText;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "rate_value", precision = 20, scale = 8)
    private BigDecimal rateValue;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
}








