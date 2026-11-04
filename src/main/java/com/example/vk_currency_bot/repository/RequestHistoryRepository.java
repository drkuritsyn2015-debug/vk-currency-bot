package com.example.vk_currency_bot.repository;

import com.example.vk_currency_bot.entity.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Long> {
    List<RequestHistory> findTop5ByVkUserIdOrderByRequestDateDesc(Long vkUserId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM RequestHistory rh Where rh.requestDate< :olderThan")
    int deleteOldRequests(@Param("olderThan") LocalDateTime olderThan);
}