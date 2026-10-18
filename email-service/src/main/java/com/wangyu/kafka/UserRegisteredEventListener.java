package com.wangyu.kafka;

import com.wangyu.mail.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka에서 가입 이벤트를 컨슘해 메일을 발송하는 비동기 워커다.
 * [MUST] REST API로 직접 호출하지 않는다 - 이 클래스가 email-service의 유일한 진입점이다.
 */
@Component
public class UserRegisteredEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredEventListener.class);

    private final EmailSender emailSender;

    public UserRegisteredEventListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @KafkaListener(topics = "${email-service.kafka.user-registered-topic}", groupId = "email-service")
    public void onUserRegistered(UserRegisteredEvent event) {
        if (event.getActivationCode() == null || event.getActivationCode().isBlank()) {
            log.warn("Received user-registered event without activation code for {}", event.getEmail());
            return;
        }
        emailSender.sendActivationEmail(event.getEmail(), event.getNickname(), event.getActivationCode());
    }
}
