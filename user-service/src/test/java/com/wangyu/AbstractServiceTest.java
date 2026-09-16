package com.wangyu;

import com.wangyu.kafka.UserRegisteredEventProducer;
import com.wangyu.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * architecture.md 4.4: 서비스 단위 테스트는 Repository/Kafka Producer를 @MockBean으로 대체해
 * 실제 DB/Kafka 접속 없이 돌아간다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractServiceTest {

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected UserRegisteredEventProducer userRegisteredEventProducer;
}
