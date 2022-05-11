package com.shareit.service.client;

import com.shareit.domain.dto.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaClient {

    Media getMediaById(Long mediaId);

    List<Media> createMedias(MultipartFile[] file);

    Media updateMedia(Long mediaId, MultipartFile file);
}
