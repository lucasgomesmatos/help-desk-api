package br.com.helpdesk.controllers.impl;

import br.com.helpdesk.controllers.OrderController;
import models.requests.CreateOrderRequest;
import org.springframework.http.ResponseEntity;

public class OrderControllerImpl implements OrderController {

    @Override
    public ResponseEntity<Void> save(CreateOrderRequest createOrderRequest) {
        return null;
    }
}
