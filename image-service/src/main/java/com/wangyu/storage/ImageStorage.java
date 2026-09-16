package com.wangyu.storage;

public interface ImageStorage {

    /**
     * 이미지 바이트를 오브젝트 스토리지에 업로드하고 외부에서 접근 가능한 URL을 반환한다.
     * @param objectKey 스토리지 내 객체 키(파일명)
     * @param content 업로드할 바이트 내용
     * @param contentType 업로드할 파일의 MIME 타입
     * @param contentLength 업로드할 바이트 길이
     * @return 업로드된 객체에 접근 가능한 URL
     * @throws com.wangyu.ApiRequestException 업로드 자체가 실패한 경우
     */
    String upload(String objectKey, byte[] content, String contentType, long contentLength);
}
