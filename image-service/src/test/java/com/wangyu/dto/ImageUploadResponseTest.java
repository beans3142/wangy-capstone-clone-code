package com.wangyu.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageUploadResponseTest {

    @Test
    public void exposesUrlThroughConstructor() {
        ImageUploadResponse response = new ImageUploadResponse("http://localhost:9000/wangyu-images/cat.png");

        assertEquals("http://localhost:9000/wangyu-images/cat.png", response.getUrl());
    }

    @Test
    public void exposesUrlThroughNoArgConstructorAndSetter() {
        ImageUploadResponse response = new ImageUploadResponse();
        response.setUrl("http://localhost:9000/wangyu-images/dog.png");

        assertEquals("http://localhost:9000/wangyu-images/dog.png", response.getUrl());
    }
}
