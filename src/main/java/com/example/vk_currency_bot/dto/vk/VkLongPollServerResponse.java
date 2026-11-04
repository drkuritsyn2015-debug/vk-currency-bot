
package com.example.vk_currency_bot.dto.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkLongPollServerResponse {
    private String key;
    private String server;
    private String ts;
}
