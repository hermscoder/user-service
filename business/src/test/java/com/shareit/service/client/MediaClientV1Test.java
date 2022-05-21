package com.shareit.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shareit.domain.dto.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MediaClientV1Test {

    private MediaClient mediaClient;
    private WebClient.Builder webClientBuilder;

    private final Media expectedMedia = Media.builder().id(1L).type("IMAGE").url("http://test.url.com").build();
    private final String expectedMediaStr = objectToJsonString(expectedMedia);
    private final String fileName = "test.png";

    @BeforeEach
    public void setup() {
        this.webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> {
                    HttpStatus httpStatus = HttpStatus.OK;
                    String body = expectedMediaStr;
                    if(clientRequest.method().equals(HttpMethod.GET)) {
                        httpStatus = HttpStatus.OK;
                        body = expectedMediaStr;
                    } else if (clientRequest.method().equals(HttpMethod.POST)) {
                        httpStatus = HttpStatus.OK;
                        body = "[" + expectedMediaStr + "]";
                    } else if (clientRequest.method().equals(HttpMethod.PUT)) {
                        httpStatus = HttpStatus.OK;
                        body = expectedMediaStr;
                    }

                    return Mono.just(ClientResponse.create(httpStatus)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(body)
                            .build());
                });
        this.mediaClient = new MediaClientV1(webClientBuilder);
    }

    @Test
    void getMediaById() {
        Media mediaById = mediaClient.getMediaById(1L);
        assertNotNull(mediaById);
        assertEquals(expectedMedia, mediaById);
    }

    @Test
    void createMedias() {
        List<Media> createdMedias = mediaClient.createMedias(new MultipartFile[] {new MockMultipartFile(fileName, new byte[10])});
        assertNotNull(createdMedias);
        assertTrue(createdMedias.size() == 1);
        assertEquals(expectedMedia, createdMedias.get(0));
    }

    @Test
    void updateMedia() {
        Media updatedMedia = mediaClient.updateMedia(1L, new MockMultipartFile(fileName, new byte[10]));
        assertNotNull(updatedMedia);
        assertEquals(expectedMedia, updatedMedia);
    }

    private String objectToJsonString(Media media) {
        ObjectMapper mapper = new ObjectMapper();
        String s = null;
        try {
            s = mapper.writeValueAsString(expectedMedia);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

}