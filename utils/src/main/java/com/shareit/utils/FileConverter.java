package com.shareit.utils;

import com.shareit.utils.commons.exception.MediaUploadException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileConverter {

    public static MultiValueMap<String, HttpEntity<?>> convert(MultipartFile... files) {
        try {
            return multipartFileToMultipartBody(files);
        } catch (IOException e) {
            throw new MediaUploadException("Error while converting media: " + e.getMessage(), e);
        }
    }

    private static MultiValueMap<String, HttpEntity<?>> multipartFileToMultipartBody(MultipartFile... files) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        for(int i = 0; i< files.length; i++) {
            MultipartFile multipartFile = files[i];
            builder.part("file", new FileNameAwareByteArrayResource(multipartFile.getOriginalFilename(), multipartFile.getBytes()));
        }
        return builder.build();
    }

    static class FileNameAwareByteArrayResource extends ByteArrayResource {

        private String fileName;

        public FileNameAwareByteArrayResource(String fileName, byte[] byteArray) {
            super(byteArray);
            this.fileName = fileName;
        }

        @Override
        public String getFilename() {
            return fileName;
        }
    }
}
