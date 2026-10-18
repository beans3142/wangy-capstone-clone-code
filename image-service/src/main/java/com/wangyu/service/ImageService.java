package com.wangyu.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.wangyu.ApiRequestException;
import com.wangyu.storage.ImageStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ImageStorage imageStorage;
    private final long maxFileSizeBytes;

    public ImageService(ImageStorage imageStorage,
                         @Value("${image-service.upload.max-file-size-bytes}") long maxFileSizeBytes) {
        this.imageStorage = imageStorage;
        this.maxFileSizeBytes = maxFileSizeBytes;
    }

    public String uploadImage(MultipartFile file) {
        byte[] content = readBytes(validateNotEmpty(file));
        validateSize(content);
        validateIsImage(file.getContentType(), content);

        String objectKey = buildObjectKey(file.getOriginalFilename());
        return imageStorage.upload(objectKey, content, file.getContentType(), content.length);
    }

    private MultipartFile validateNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiRequestException("업로드할 파일이 비어 있습니다", HttpStatus.BAD_REQUEST);
        }
        return file;
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException exception) {
            throw new ApiRequestException("파일을 읽을 수 없습니다", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSize(byte[] content) {
        if (content.length > maxFileSizeBytes) {
            throw new ApiRequestException("파일 크기가 허용 범위를 초과했습니다", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsImage(String contentType, byte[] content) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiRequestException("이미지 파일만 업로드할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
        if (!isDecodableImage(content)) {
            throw new ApiRequestException("이미지 파일만 업로드할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isDecodableImage(byte[] content) {
        try {
            return ImageIO.read(new ByteArrayInputStream(content)) != null;
        } catch (IOException exception) {
            return false;
        }
    }

    private String buildObjectKey(String originalFilename) {
        String extension = extractExtension(originalFilename);
        return UUID.randomUUID() + extension;
    }

    private String extractExtension(String originalFilename) {
        if (!StringUtils.hasText(originalFilename) || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }
}
