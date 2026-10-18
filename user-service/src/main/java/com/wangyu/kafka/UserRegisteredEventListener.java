package com.wangyu.kafka;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 회원가입 트랜잭션이 실제로 커밋된 뒤에만 Kafka로 이벤트를 발행한다.
 * 트랜잭션이 롤백되면 이 리스너 자체가 호출되지 않으므로, 존재하지 않는 계정에 대한
 * 인증 메일이 나가는 상황을 막는다.
 */
@Component
public class UserRegisteredEventListener {

    private final UserRegisteredEventProducer producer;

    public UserRegisteredEventListener(UserRegisteredEventProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        producer.publish(event);
    }
}
