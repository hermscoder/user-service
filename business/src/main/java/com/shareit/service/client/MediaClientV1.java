package com.shareit.service.client;

import com.shareit.domain.dto.Media;
import com.shareit.utils.FileConverter;
import com.shareit.utils.commons.exception.MediaUploadException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class MediaClientV1 implements MediaClient {

    private static final String MEDIA_SERVICE_URL = "http://media-service/v1/media";
    private final WebClient webClient;

    public MediaClientV1(WebClient webClient) {
        this.webClient = webClient;
    }

    public Media getMediaById(Long mediaId) throws MediaUploadException {
        Media userMedia = null;
        try{
            userMedia = webClient.get()
                    .uri(MEDIA_SERVICE_URL, uriBuilder -> uriBuilder.pathSegment(String.valueOf(mediaId)).build())
                    .retrieve()
                    .bodyToMono(Media.class)
                    .doOnError((error) -> new MediaUploadException("Error while fetching media", error))
                    .block();
        } catch (Exception e) {
            throw new MediaUploadException("Error while fetching media", e);
        }
        return userMedia;
    }

    @Override
    public List<Media> createMedias(MultipartFile[] files) throws MediaUploadException {
        return webClient.post()
                .uri(MEDIA_SERVICE_URL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(FileConverter.convert(files)))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Media>>() {})
                .doOnError((error) -> new MediaUploadException("Error while creating media", error))
                .block();
    }

    @Override
    public Media updateMedia(Long mediaId, MultipartFile file) throws MediaUploadException {
        return webClient.put()
                .uri(MEDIA_SERVICE_URL, uriBuilder -> uriBuilder.pathSegment(String.valueOf(mediaId)).build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(FileConverter.convert(file)))
                .retrieve()
                .bodyToMono(Media.class)
                .doOnError((error) -> new MediaUploadException("Error while updating media", error))
                .block();
    }


}
