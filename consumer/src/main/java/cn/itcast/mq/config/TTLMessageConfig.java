package cn.itcast.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TTLMessageConfig {
    /**
     * @description 死信交换机声明
     * @author yanglei
     * @date 2023/4/8 19:22
     * @return DirectExchange
     */
    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("dl.direct",true,false);
    }

    /**
     * @description 死信队列声明
     * @author yanglei
     * @date 2023/4/8 19:22
     * @return Queue
     */
    @Bean
    public Queue deadLeterQueue(){
        return QueueBuilder.durable("dl.queue")
                .ttl(10000) //设置过期时间
                .deadLetterExchange("dl.direct")//绑定死信交换机
                .deadLetterRoutingKey("dl")//添加路由key
                .build();
    }
    /**
     * @description 声明死信队列和死信交换机的绑定关系
     * @author yanglei
     * @date 2023/4/8 19:23
     * @return Binding
     */
    @Bean
    public Binding ttlBinding(){
        return BindingBuilder
                .bind(deadLeterQueue())
                .to(deadLetterExchange())
                .with("ttl");
    }

}

