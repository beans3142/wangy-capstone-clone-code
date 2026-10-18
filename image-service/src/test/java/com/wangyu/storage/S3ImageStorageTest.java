package com.wangyu.storage;

import java.net.MalformedURLException;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wangyu.ApiRequestException;
import com.wangyu.config.S3Properties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class S3ImageStorageTest {

    private AmazonS3 amazonS3;
    private S3ImageStorage storage;

    @Before
    public void setUp() {
        amazonS3 = mock(AmazonS3.class);
        S3Properties properties = new S3Properties();
        properties.setBucket("wangyu-images");
        storage = new S3ImageStorage(amazonS3, properties);
    }

    @Test
    public void uploadsObjectAndReturnsPublicUrl() throws MalformedURLException {
        when(amazonS3.getUrl(eq("wangyu-images"), eq("cat.png")))
                .thenReturn(new URL("http://localhost:9000/wangyu-images/cat.png"));

        String url = storage.upload("cat.png", new byte[]{1, 2, 3}, "image/png", 3);

        assertEquals("http://localhost:9000/wangyu-images/cat.png", url);
        verify(amazonS3).putObject(any(PutObjectRequest.class));
    }

    @Test
    public void wrapsS3FailureAsInternalServerError() {
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenThrow(new AmazonClientException("boom"));

        try {
            storage.upload("cat.png", new byte[]{1, 2, 3}, "image/png", 3);
            fail("ApiRequestException을 기대했지만 발생하지 않았습니다");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        }
    }
}
