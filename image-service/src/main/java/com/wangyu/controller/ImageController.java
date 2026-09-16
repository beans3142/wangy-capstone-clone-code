package com.wangyu.controller;

import com.wangyu.dto.ImageUploadResponse;
import com.wangyu.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * 이미지 파일을 받아 오브젝트 스토리지에 업로드하고 접근 가능한 URL을 반환한다.
     * @param file 업로드할 이미지 파일(multipart/form-data)
     * @return 업로드된 이미지에 접근 가능한 URL
     * @throws com.wangyu.ApiRequestException 파일이 비어있거나, 이미지가 아니거나, 크기 제한을 초과하거나, 업로드가 실패한 경우
     */
    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = imageService.uploadImage(file);
        return ResponseEntity.ok(new ImageUploadResponse(url));
    }
}
