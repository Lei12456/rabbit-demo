package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        // 1.准备消息
        String message = "hello";
        //2、准备correlationData，携带消息的唯一标识的数据
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //3、准备确认消息回调通知 confirmCallback
        //成功消息回调
        //失败的消息回调
        correlationData.getFuture().addCallback(confirm -> {
            assert confirm != null;
            if (confirm.isAck()){
                //ACK
                //log.info("消息发送到队列成功，消息ID:{}",correlationData.getId());
            }else {
                //NACK
                //log.error("消息发送到队列失败，消息ID:{}",correlationData.getId());
            }
        }, throwable -> {
            //log.error("消息发送失败",throwable);
        });
        //2、发送消息
        rabbitTemplate.convertAndSend("amq.topic","simple.tt",message,correlationData);

    }

    @Test
    public void testDurableMessage() {
        // 1.准备消息，持久化消息
        Message message = MessageBuilder.withBody("hello, spring".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        // 2.发送消息
        rabbitTemplate.convertAndSend("simple.queue", message);
    }

    @Test
    public void testTTLMessage() {

    }

    @Test
    public void testSendDelayMessage() throws InterruptedException {
    }

    @Test
    public void testLazyQueue() throws InterruptedException {

    }
    @Test
    public void testNormalQueue() throws InterruptedException {

    }
}
