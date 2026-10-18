package com.wangyu.storage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.wangyu.config.S3Properties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * 로컬에 이미 떠 있는 MinIO(wangyu-minio, http://localhost:9000)에 실제로 업로드하고
 * 반환된 URL로 다시 내려받아 바이트가 일치하는지 검증한다. mock을 쓰지 않는다.
 */
public class S3ImageStorageIntegrationTest {

    private static final String ENDPOINT = "http://localhost:9000";

    private S3ImageStorage storage;

    @Before
    public void setUp() {
        assumeTrue("로컬 MinIO(localhost:9000)가 떠 있지 않아 통합 테스트를 건너뜁니다", isMinioReachable());

        S3Properties properties = new S3Properties();
        properties.setEndpoint(ENDPOINT);
        properties.setRegion("us-east-1");
        properties.setAccessKey("wangyu-dev");
        properties.setSecretKey("wangyu-dev-secret");
        properties.setBucket("wangyu-images");

        BasicAWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .build();

        storage = new S3ImageStorage(amazonS3, properties);
    }

    @Test
    public void uploadedImageIsDownloadableFromReturnedUrl() throws IOException, InterruptedException {
        byte[] content = realPngBytes();
        String objectKey = UUID.randomUUID() + ".png";

        String url = storage.upload(objectKey, content, "image/png", content.length);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        assertEquals(200, response.statusCode());
        assertArrayEquals(content, response.body());
    }

    private boolean isMinioReachable() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT + "/minio/health/live")).GET().build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException exception) {
            return false;
        }
    }

    private byte[] realPngBytes() throws IOException {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}
