package com.wangyu.storage;

import java.io.ByteArrayInputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wangyu.ApiRequestException;
import com.wangyu.config.S3Properties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class S3ImageStorage implements ImageStorage {

    private final AmazonS3 amazonS3;
    private final S3Properties s3Properties;

    public S3ImageStorage(AmazonS3 amazonS3, S3Properties s3Properties) {
        this.amazonS3 = amazonS3;
        this.s3Properties = s3Properties;
    }

    @Override
    public String upload(String objectKey, byte[] content, String contentType, long contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);

        try {
            PutObjectRequest request = new PutObjectRequest(
                    s3Properties.getBucket(), objectKey, new ByteArrayInputStream(content), metadata);
            amazonS3.putObject(request);
        } catch (AmazonClientException exception) {
            throw new ApiRequestException("이미지 업로드에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return amazonS3.getUrl(s3Properties.getBucket(), objectKey).toExternalForm();
    }
}
