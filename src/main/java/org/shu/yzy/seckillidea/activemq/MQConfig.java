package org.shu.yzy.seckillidea.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String DESTINATION = "seckill";

    @Bean(name = "seckillQueue")
    public ActiveMQQueue seckillQueue(){
        return new ActiveMQQueue(DESTINATION);
    }
}
