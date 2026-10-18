package com.wangyu.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.wangyu.ApiRequestException;
import com.wangyu.storage.ImageStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceTest {

    private static final long MAX_FILE_SIZE_BYTES = 1024;

    private ImageStorage imageStorage;
    private ImageService imageService;

    @Before
    public void setUp() {
        imageStorage = mock(ImageStorage.class);
        imageService = new ImageService(imageStorage, MAX_FILE_SIZE_BYTES);
    }

    @Test
    public void uploadsValidImageAndReturnsUrl() throws IOException {
        byte[] pngBytes = realPngBytes();
        MultipartFile file = new MockMultipartFile("file", "cat.png", "image/png", pngBytes);
        when(imageStorage.upload(anyString(), eq(pngBytes), eq("image/png"), eq((long) pngBytes.length)))
                .thenReturn("http://localhost:9000/wangyu-images/generated.png");

        String url = imageService.uploadImage(file);

        assertEquals("http://localhost:9000/wangyu-images/generated.png", url);
    }

    @Test
    public void generatedObjectKeyKeepsOriginalExtension() throws IOException {
        byte[] pngBytes = realPngBytes();
        MultipartFile file = new MockMultipartFile("file", "cat.png", "image/png", pngBytes);
        when(imageStorage.upload(anyString(), eq(pngBytes), anyString(), anyLong()))
                .thenReturn("http://localhost:9000/wangyu-images/generated.png");

        imageService.uploadImage(file);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(imageStorage).upload(keyCaptor.capture(), eq(pngBytes), anyString(), anyLong());
        assertTrue(keyCaptor.getValue().endsWith(".png"));
    }

    @Test
    public void rejectsEmptyFile() {
        MultipartFile file = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

        assertRejected(file, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void rejectsNullContentType() {
        MultipartFile file = new MockMultipartFile("file", "cat", null, new byte[]{1, 2, 3});

        assertRejected(file, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void rejectsNonImageContentType() {
        MultipartFile file = new MockMultipartFile("file", "virus.exe", "application/x-msdownload", new byte[]{1, 2, 3});

        assertRejected(file, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void rejectsSpoofedContentTypeWhenBytesAreNotADecodableImage() {
        MultipartFile file = new MockMultipartFile("file", "virus.png", "image/png", new byte[]{1, 2, 3, 4});

        assertRejected(file, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void rejectsFileLargerThanConfiguredLimit() {
        byte[] oversized = new byte[(int) MAX_FILE_SIZE_BYTES + 1];
        MultipartFile file = new MockMultipartFile("file", "big.png", "image/png", oversized);

        assertRejected(file, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void doesNotCallStorageWhenValidationFails() {
        MultipartFile file = new MockMultipartFile("file", "virus.exe", "application/x-msdownload", new byte[]{1, 2, 3});

        assertRejected(file, HttpStatus.BAD_REQUEST);

        verify(imageStorage, never()).upload(anyString(), (byte[]) org.mockito.ArgumentMatchers.any(), anyString(), anyLong());
    }

    private void assertRejected(MultipartFile file, HttpStatus expectedStatus) {
        try {
            imageService.uploadImage(file);
            fail("ApiRequestException을 기대했지만 발생하지 않았습니다");
        } catch (ApiRequestException exception) {
            assertEquals(expectedStatus, exception.getStatus());
        }
    }

    private byte[] realPngBytes() throws IOException {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}
