package guru.sfg.beer.inventory.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@RequiredArgsConstructor
public class JmsConfig {
    public static final String BREWING_REQUEST_QUEUE_NAME = "brewing-request";
    public static final String NEW_INVENTORY_QUEUE_NAME = "new-inventory";
    public static final String ALLOCATE_ORDER_QUEUE_NAME = "allocate-order";
    public static final String ALLOCATE_ORDER_RESULT_QUEUE_NAME = "allocate-order-result";
    public static final String DEALLOCATE_ORDER_QUEUE_NAME = "deallocate-order";

    private final ObjectMapper objectMapper;

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
