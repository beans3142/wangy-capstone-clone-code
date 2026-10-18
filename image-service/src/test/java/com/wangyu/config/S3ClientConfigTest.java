package com.wangyu.config;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class S3ClientConfigTest {

    @Test
    public void buildsAmazonS3ClientFromProperties() {
        S3Properties properties = new S3Properties();
        properties.setEndpoint("http://localhost:9000");
        properties.setRegion("us-east-1");
        properties.setAccessKey("wangyu-dev");
        properties.setSecretKey("wangyu-dev-secret");
        properties.setBucket("wangyu-images");

        AmazonS3 amazonS3 = new S3ClientConfig().amazonS3(properties);

        assertNotNull(amazonS3);
    }
}
