package guru.sfg.beer.inventory.service.listeners;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.events.BeerDto;
import guru.sfg.brewery.model.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewInventoryListener {
    private final BeerInventoryRepository inventoryRepository;


    @Transactional
    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE_NAME)
    public void listenForNewInventory(NewInventoryEvent event) {
        BeerDto beerDto = event.getBeerDto();
        BeerInventory beerInventory = BeerInventory.builder()
                                                              .beerId(beerDto.getId())
                                                              .quantityOnHand(beerDto.getQuantityOnHand())
                                                              .upc(beerDto.getUpc())
                                                              .build();
        log.debug(String.format("Creating new inventory for Beer: %s", beerDto));
        inventoryRepository.save(beerInventory);
    }
}
