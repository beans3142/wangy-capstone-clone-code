package com.wangyu.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 로컬 개발 인프라에 SMTP 서버가 없으므로(architecture.md 4.4에 명시된 인프라는 Postgres/Kafka뿐),
 * 실제 메일 전송 대신 로그로 대체한다. EmailSender 인터페이스 뒤에 있으므로 실제 SMTP 연동은
 * 이 구현체만 교체하면 된다.
 */
@Component
public class LoggingEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailSender.class);

    @Override
    public void sendActivationEmail(String toEmail, String nickname, String activationCode) {
        log.info("[email-service] {}님({})께 인증 메일 발송 - activationCode={}", nickname, toEmail, activationCode);
    }
}
