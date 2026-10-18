package com.wangyu.controller;

import com.wangyu.dto.ImageUploadResponse;
import com.wangyu.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageControllerTest {

    private ImageService imageService;
    private ImageController controller;

    @Before
    public void setUp() {
        imageService = mock(ImageService.class);
        controller = new ImageController(imageService);
    }

    @Test
    public void uploadImageReturns200WithUrl() {
        MultipartFile file = new MockMultipartFile("file", "cat.png", "image/png", new byte[]{1, 2, 3});
        when(imageService.uploadImage(file)).thenReturn("http://localhost:9000/wangyu-images/cat.png");

        ResponseEntity<ImageUploadResponse> result = controller.uploadImage(file);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("http://localhost:9000/wangyu-images/cat.png", result.getBody().getUrl());
    }
}
