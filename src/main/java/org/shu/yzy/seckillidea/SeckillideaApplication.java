package org.shu.yzy.seckillidea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.shu.yzy"})
@ServletComponentScan(basePackages = {"org.shu.yzy.seckillidea.druid"})
public class SeckillideaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillideaApplication.class, args);
    }
}
