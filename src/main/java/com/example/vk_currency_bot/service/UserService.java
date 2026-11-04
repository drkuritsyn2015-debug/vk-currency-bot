
package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.repository.VkUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final VkUserRepository userRepository;

    @Transactional
    public VkUser getOrCreateUser(Long vkUserId) {
        return userRepository.findById(vkUserId).orElseGet(() -> {
            VkUser newUser = VkUser.builder()
                    .vkUserId(vkUserId)
                    .dialogState(DialogState.NEW_USER)
                    .build();
            log.info("Создан новый пользователь с ID: {}", vkUserId);
            return userRepository.save(newUser);
        });
    }

    @Transactional
    public void updateState(VkUser user, DialogState newState) {
        user.updateDialogState(newState);
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(VkUser user, String firstName, String lastName) {
        user.updateName(firstName, lastName);
        userRepository.save(user);
    }
}