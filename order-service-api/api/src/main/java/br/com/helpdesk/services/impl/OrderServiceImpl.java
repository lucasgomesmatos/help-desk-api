package br.com.helpdesk.services.impl;

import br.com.helpdesk.mapper.OrderMapper;
import br.com.helpdesk.repositories.OrderRepository;
import br.com.helpdesk.services.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final OrderMapper mapper;

    @Override
    public void save(CreateOrderRequest request) {
        repository.save(
                mapper.fromRequest(request)
        );
    }
}
