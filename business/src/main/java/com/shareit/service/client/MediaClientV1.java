package com.shareit.service.client;

import com.shareit.domain.dto.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MediaClientV1 implements MediaClient {

    private final WebClient webClient;

    public MediaClientV1(WebClient webClient) {
        this.webClient = webClient;
    }

    public Media getMediaById(Long mediaId) {
        Media userMedia = webClient.get()
                .uri("http://localhost:8082/v1/media",
                        uriBuilder -> uriBuilder.build(mediaId))
                .retrieve()
                .bodyToMono(Media.class)
                .block();

        return userMedia;
    }
}
