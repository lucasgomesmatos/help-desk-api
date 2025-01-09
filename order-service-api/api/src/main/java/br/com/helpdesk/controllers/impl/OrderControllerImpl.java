package br.com.helpdesk.controllers.impl;

import br.com.helpdesk.controllers.OrderController;
import br.com.helpdesk.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreateOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @Override
    public ResponseEntity<Void> save(CreateOrderRequest createOrderRequest) {
        service.save(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
