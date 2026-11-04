package com.example.vk_currency_bot.repository;

import com.example.vk_currency_bot.entity.VkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VkUserRepository extends JpaRepository<VkUser, Long> {
}