package ru.neoflex.gateway.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.neoflex.gateway.client.RetrieveMessageErrorDecoder;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetrieveMessageErrorDecoder();
    }
}
