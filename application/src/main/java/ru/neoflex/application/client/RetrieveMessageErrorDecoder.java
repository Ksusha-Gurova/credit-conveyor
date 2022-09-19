package ru.neoflex.application.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import ru.neoflex.application.api.ErrorMessage;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class RetrieveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            message = mapper.readValue(bodyIs, ErrorMessage.class);
        } catch (IOException e) {
            return new IllegalArgumentException(e.getMessage());
        }
        if (response.status() != 200) {
            log.error("статус: " + response.status() + ", сообщение: " + message.getMessage());
            throw new IllegalArgumentException(message.getMessage());
        }
        return errorDecoder.decode(methodKey, response);
    }
}
