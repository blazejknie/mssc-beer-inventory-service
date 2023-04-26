package guru.sfg.beer.inventory.service.services.listeners;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocationService;
import guru.sfg.brewery.model.AllocateOrderRequest;
import guru.sfg.brewery.model.AllocationOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderAllocationListener {
    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE_NAME)
    public void processOrderAllocation(AllocateOrderRequest request) {
        AllocationOrderResponse.AllocationOrderResponseBuilder builder = AllocationOrderResponse.builder()
                                                                                                .beerOrderDto(
                                                                                                        request.getBeerOrderDto());
        try {
            Boolean allocationResult = allocationService.allocateOrder(request.getBeerOrderDto());
            builder.isPendingInventory(!allocationResult);
        } catch (Exception e) {
            log.error("Allocation failed for Order Id :" + request.getBeerOrderDto().getId());
            builder.isAllocationError(true);
        }

        AllocationOrderResponse response = builder.build();
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE_NAME, response);
        log.debug("sent Allocation response:" + response);
    }

}
