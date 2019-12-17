package com.github.kaguya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class KaguyaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(KaguyaApplication.class);
        application.run(args);
    }

}
