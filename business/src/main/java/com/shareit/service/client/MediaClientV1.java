package com.shareit.service.client;

import com.shareit.domain.dto.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MediaClientV1 implements MediaClient {

    private static final String MEDIA_SERVICE_URL = "http://localhost:8082/v1/media";
    private final WebClient webClient;

    public MediaClientV1(WebClient webClient) {
        this.webClient = webClient;
    }

    public Media getMediaById(Long mediaId) {
        Media userMedia = webClient.get()
                .uri(MEDIA_SERVICE_URL, uriBuilder -> uriBuilder.pathSegment(String.valueOf(mediaId)).build())
                .retrieve()
                .bodyToMono(Media.class)
                .block();

        return userMedia;
    }
}
